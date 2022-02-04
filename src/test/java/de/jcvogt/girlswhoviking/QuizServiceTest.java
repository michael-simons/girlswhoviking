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

import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

/**
 * @author Michael J. Simons
 */
@QuarkusTest
class QuizServiceTest {

	@Inject
	QuizService quizService;

	@Test
	void shouldLoadSteps() {

		var questions = quizService.getQuestions();
		assertThat(questions)
			.hasSize(2)
			.first()
			.satisfies(q -> {
				assertThat(q.value()).isEqualTo("Was isst Du lieber?");
				assertThat(q.answers())
					.hasSize(2)
					.allMatch(a -> a.increments().size() == 2)
					.extracting(Quiz.Answer::value)
					.containsExactly("Chips", "Schokolade");
			});
	}
}
