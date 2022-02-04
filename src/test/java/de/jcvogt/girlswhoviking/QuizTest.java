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

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Michael J. Simons
 */
class QuizTest {

	@Test
	void shouldBeDoneWithoutQuestions() {

		var quiz = new Quiz(List.of());
		assertThat(quiz.isDone()).isTrue();
		assertThat(quiz.getCurrentQuestion()).isEmpty();
		assertThat(quiz.evaluate(42)).isTrue();
		assertThat(quiz.getResult()).isEmpty();
	}

	@Test
	void shouldAdvanceAfterEvaluate() {

		var question = new Quiz.Question("Was ist die Antwort?", List.of());
		var quiz = new Quiz(List.of(question));
		assertThat(quiz.getCurrentQuestion())
			.hasValueSatisfying(c -> {
					assertThat(c.last()).isTrue();
					assertThat(c.idx()).isOne();
					assertThat(c.question()).isEqualTo("Was ist die Antwort?");
				}
			);
		assertThat(quiz.evaluate(42)).isTrue();
		assertThat(quiz.getCurrentQuestion()).isEmpty();
	}
}
