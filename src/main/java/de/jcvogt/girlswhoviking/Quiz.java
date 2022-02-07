/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jcvogt.girlswhoviking;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author Michael J. Simons
 */
public class Quiz implements Serializable {

	/**
	 * Represents one answer with its increments for the various results.
	 *
	 * @param value      the textual value of the answer
	 * @param increments the list of increments
	 */
	public record Answer(String value, List<Integer> increments) {
		public Answer {
			increments = increments == null ? Collections.emptyList() : List.copyOf(increments);
		}
	}

	/**
	 * Represents a questions with its possible answers.
	 *
	 * @param value   the textual value of the question
	 * @param answers the list of answers
	 */
	public record Question(String value, List<Answer> answers) {
		public Question {
			answers = answers == null ? Collections.emptyList() : List.copyOf(answers);
		}
	}

	/**
	 * The possible outcome of a quiz
	 *
	 * @param name        The name of the person
	 * @param title       The persons title
	 * @param description A description
	 * @param quote       A quote
	 */
	public record Outcome(String name, String title, String description, String quote) {

		public String formattedQuote() {
			return "\"" + quote() + "\"\n- " + name + "\nMach auch Du das Quiz:";
		}
	}

	/**
	 * The definition of a quiz consists of
	 *
	 * @param outcomes  some possible outcomes
	 * @param questions and a number of questions
	 */
	public record Definition(List<Outcome> outcomes, List<Question> questions) {
		public Definition {
			outcomes = outcomes == null ? Collections.emptyList() : List.copyOf(outcomes);
			questions = questions == null ? Collections.emptyList() : List.copyOf(questions);
		}
	}

	/**
	 * Represents the current question to be answered.
	 *
	 * @param value   the underlying question
	 * @param idx     the 1-based-index in the list of questions
	 * @param last    a flag if this is the last question
	 * @param indexes randomized indexes
	 */
	public record CurrentQuestion(Question value, int idx, boolean last, List<Integer> indexes) {

		public CurrentQuestion {
			if (indexes.size() != value.answers.size()) {
				throw new IllegalArgumentException("Invalid number of indexes.");
			}
			if (indexes.stream().anyMatch(i -> i < 0 || i >= value.answers().size())) {
				throw new IllegalArgumentException("Invalid index in index list.");
			}
		}

		public CurrentQuestion(Question value, int idx, boolean last) {
			this(value, idx, last, IntStream.range(0, value.answers().size()).boxed()
				.collect(collectingAndThen(toList(), l -> {
					Collections.shuffle(l);
					return List.copyOf(l);
				})));
		}

		public String question() {
			return value.value();
		}

		public List<Answer> answers() {
			return value.answers();
		}
	}

	@Serial
	private static final long serialVersionUID = -7361338223494472755L;

	private final Definition definition;
	private final List<Question> questions;

	private int counter = 0;

	private final Map<Integer, Integer> points = new TreeMap<>();

	private Outcome outcome;

	public Quiz(Definition definition) {
		this.definition = definition;
		this.questions = definition.questions();
	}

	public boolean isDone() {
		return this.counter >= questions.size();
	}

	public Optional<CurrentQuestion> getCurrentQuestion() {

		if (isDone()) {
			return Optional.empty();
		}

		var idx = this.counter;
		return Optional.of(
			new CurrentQuestion(questions.get(idx), idx + 1, idx == this.questions.size() - 1));
	}

	public boolean evaluate(Integer selectedAnswer) {

		if (isDone()) {
			return true;
		}

		var selectedQuestions = questions.get(this.counter);
		var answers = selectedQuestions.answers();

		Objects.requireNonNull(selectedAnswer);
		if (selectedAnswer < 0 || selectedAnswer >= answers.size()) {
			throw new IllegalArgumentException("Illegal answer (%d)".formatted(selectedAnswer));
		}

		var increments = answers.get(selectedAnswer).increments();
		if (increments.size() != answers.size()) {
			var value = this.points.getOrDefault(selectedAnswer, 0);
			this.points.put(selectedAnswer, value + 1);
		} else {
			for (int i = 0; i < increments.size(); i++) {
				var increment = increments.get(i);
				var value = this.points.getOrDefault(i, 0);
				this.points.put(i, value + increment);
			}
		}

		++this.counter;
		return isDone();
	}

	// We do check this by checking if there's something in the answer list
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public Optional<Outcome> getResult() {

		if (this.questions.isEmpty() || !this.isDone()) {
			return Optional.empty();
		}

		if (this.outcome == null) {
			// Cache this so that a possible randomization of a non-unique answer is stable
			this.outcome = this.points.entrySet().stream().max(Map.Entry.comparingByValue())
				.map(maxEntry -> {
					var allEntriesWithValue = points.entrySet().stream()
						.filter(e -> e.getValue().equals(maxEntry.getValue()))
						.toList();

					if (allEntriesWithValue.size() == 1) {
						return maxEntry;
					} else {
						var index = ThreadLocalRandom.current().nextInt(0, allEntriesWithValue.size());
						return allEntriesWithValue.get(index);
					}
				})
				.map(Map.Entry::getKey)
				.map(definition.outcomes()::get)
				.get();
		}

		return Optional.of(this.outcome);
	}

	public void reset() {
		this.counter = 0;
		this.points.clear();
		this.outcome = null;
	}
}
