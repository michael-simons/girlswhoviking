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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * @author Michael J. Simons
 */
@Path("/")
public class IndexResource {

	private final Template indexTemplate;

	public IndexResource(Template index) {
		this.indexTemplate = Objects.requireNonNull(index, "The index template is required.");
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance get(@Context UriInfo uriInfo) {
		return indexTemplate
			.data("twitter_image", uriInfo.resolve(URI.create("/img/banner_w_1500.png")));
	}
}
