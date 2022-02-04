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