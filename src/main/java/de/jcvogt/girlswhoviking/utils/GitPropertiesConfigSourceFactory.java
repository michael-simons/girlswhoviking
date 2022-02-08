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

import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.ConfigSourceFactory;
import io.smallrye.config.PropertiesConfigSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * @author Michael J. Simons
 */
public final class GitPropertiesConfigSourceFactory implements ConfigSourceFactory {

	@Override
	public Iterable<ConfigSource> getConfigSources(ConfigSourceContext configSourceContext) {
		try {
			var gitProperties = Objects.requireNonNull(this.getClass().getClassLoader().getResource("git.properties"));
			return List.of(new PropertiesConfigSource(gitProperties));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
