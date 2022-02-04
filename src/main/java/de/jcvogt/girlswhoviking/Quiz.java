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

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
	 * Represents the current question to be answered.
	 *
	 * @param value the underlying question
	 * @param idx   the 1-based-index in the list of questions
	 * @param last  a flag if this is the last question
	 */
	public record CurrentQuestion(Question value, int idx, boolean last) {

		public String question() {
			return value.value();
		}

		public List<Answer> answers() {
			return value.answers();
		}
	}

	/**
	 * The actual result of the quiz.
	 */
	public record Result() {
	}

	@Serial
	private static final long serialVersionUID = -7361338223494472755L;

	private final List<Question> questions;

	private final AtomicInteger counter = new AtomicInteger(0);

	public Quiz(List<Question> questions) {
		this.questions = questions;
	}

	public boolean isDone() {
		return this.counter.get() >= questions.size();
	}

	public Optional<CurrentQuestion> getCurrentQuestion() {

		if (isDone()) {
			return Optional.empty();
		}

		var idx = this.counter.get();
		return Optional.of(
			new CurrentQuestion(questions.get(idx), idx + 1, idx == this.questions.size() - 1));
	}

	public boolean evaluate(Integer selectedAnswer) {

		Objects.requireNonNull(selectedAnswer);

		if (isDone()) {
			return true;
		}

		this.counter.incrementAndGet();
		return isDone();
	}

	public Optional<Result> getResult() {

		if (this.questions.isEmpty() || !this.isDone()) {
			return Optional.empty();
		}

		return Optional.of(new Result());
	}

	public void reset() {
		this.counter.set(0);
	}
}
