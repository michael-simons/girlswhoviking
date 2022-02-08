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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Michael J. Simons
 */
class QuizTest {

	@Test
	void shouldBeDoneWithoutQuestions() {

		var quiz = new Quiz(new Quiz.Definition(List.of(), List.of()));
		assertThat(quiz.isDone()).isTrue();
		assertThat(quiz.getCurrentQuestion()).isEmpty();
		assertThat(quiz.evaluate(42)).isTrue();
		assertThat(quiz.getResult()).isEmpty();
	}

	@Test
	void shouldAdvanceAfterEvaluate() {

		var question = new Quiz.Question("Was ist die Antwort?", List.of(new Quiz.Answer("42", List.of())));
		var quiz = new Quiz(new Quiz.Definition(List.of(new Quiz.Outcome("n", "t", "d", "q")), List.of(question)));
		assertThat(quiz.getCurrentQuestion())
			.hasValueSatisfying(c -> {
					assertThat(c.last()).isTrue();
					assertThat(c.idx()).isOne();
					assertThat(c.question()).isEqualTo("Was ist die Antwort?");
				}
			);
		assertThat(quiz.evaluate(0)).isTrue();
		assertThat(quiz.getCurrentQuestion()).isEmpty();
	}

	@Test
	void shouldCheckAnswerIndex() {

		var question = new Quiz.Question("Was ist die Antwort?", List.of(new Quiz.Answer("42", List.of())));
		var quiz = new Quiz(new Quiz.Definition(List.of(), List.of(question)));
		assertThatIllegalArgumentException().isThrownBy(() -> quiz.evaluate(42))
			.withMessage("Illegal answer (42)");
	}

	@Nested
	class CurrentQuestionTest {
		@Test
		void shouldCreateRandomIndexes() {

			var question = new Quiz.CurrentQuestion(new Quiz.Question("Was ist die Antwort?", List.of()), 0, true);
			assertThat(question.indexes()).isEmpty();

			var answers = List.of(
				new Quiz.Answer("a", List.of()),
				new Quiz.Answer("b", List.of()),
				new Quiz.Answer("c", List.of())
			);
			question = new Quiz.CurrentQuestion(new Quiz.Question("Was ist die Antwort?", answers), 0, true);
			assertThat(question.indexes())
				.hasSize(3)
				.containsExactlyInAnyOrder(0, 1, 2);
		}

		@Test
		void shouldFailOnWrongIndexes() {

			var answers = List.of(
				new Quiz.Answer("a", List.of()),
				new Quiz.Answer("b", List.of()),
				new Quiz.Answer("c", List.of())
			);
			assertThatIllegalArgumentException().isThrownBy(
					() -> new Quiz.CurrentQuestion(new Quiz.Question("Was ist die Antwort?", answers), 0, true, List.of()))
				.withMessage("Invalid number of indexes.");
			assertThatIllegalArgumentException().isThrownBy(
					() -> new Quiz.CurrentQuestion(new Quiz.Question("Was ist die Antwort?", answers), 0, true,
						List.of(-1, 0, 1)))
				.withMessage("Invalid index in index list.");
			assertThatIllegalArgumentException().isThrownBy(
					() -> new Quiz.CurrentQuestion(new Quiz.Question("Was ist die Antwort?", answers), 0, true,
						List.of(0, 1, 42)))
				.withMessage("Invalid index in index list.");
		}
	}
}
