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
package de.jcvogt.girlswhoviking.utils;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Michael J. Simons
 */
@Named("gitId")
@Singleton final class GitId {

	private final String abbrev;

	private final String full;

	GitId(@ConfigProperty(name = "git.commit.id.abbrev") String abbrev,
		@ConfigProperty(name = "git.commit.id.full") String full) {
		this.abbrev = abbrev;
		this.full = full;
	}

	public String getAbbrev() {
		return abbrev;
	}

	public String getFull() {
		return full;
	}
}
