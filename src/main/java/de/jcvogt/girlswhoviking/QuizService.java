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

import de.jcvogt.girlswhoviking.Quiz.Question;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Michael J. Simons
 */
@ApplicationScoped
class QuizService {

	private final List<Question> questions;

	QuizService(@ConfigProperty(name = "girlswhoviking.questions") String questions, ObjectMapper objectMapper) {
		try {
			this.questions = List.copyOf(objectMapper.readValue(
				this.getClass().getClassLoader().getResourceAsStream(questions),
				new TypeReference<>() {
				}));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	List<Question> getQuestions() {
		return this.questions;
	}

	@Produces
	@SessionScoped
	Quiz newQuiz() {
		return new Quiz(this.questions);
	}
}
