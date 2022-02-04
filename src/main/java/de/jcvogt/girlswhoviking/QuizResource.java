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

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import java.net.URI;
import java.util.Objects;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Michael J. Simons
 */
@Path("/")
public class QuizResource {

	private final Template quizTemplate;
	private final Template resultTemplate;
	private final Quiz currentQuiz;

	public QuizResource(Template quiz, Template result, Quiz currentQuiz) {

		this.quizTemplate = Objects.requireNonNull(quiz, "The quiz template is required.");
		this.resultTemplate = Objects.requireNonNull(result, "The result template is required.");
		this.currentQuiz = currentQuiz;
	}

	@Path("start")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance get() {
		if (currentQuiz.isDone()) {
			currentQuiz.reset();
		}
		return quizTemplate.data("quiz", currentQuiz);
	}

	@Path("answer")
	@POST
	public Response next(@FormParam(value = "selectedAnswer") Integer selectedAnswer) {
		var location = URI.create(selectedAnswer != null && currentQuiz.evaluate(selectedAnswer) ? "result" : "start");
		return Response.seeOther(location).build();
	}

	@Path("reset")
	@POST
	public Response reset() {
		currentQuiz.reset();
		return Response.seeOther(URI.create("start")).build();
	}

	@Path("result")
	@GET
	public Response result() {

		return currentQuiz.getResult()
			.map(r -> Response.ok(resultTemplate.instance()))
			.orElseGet(() -> Response.seeOther(URI.create("start")))
			.build();
	}
}
