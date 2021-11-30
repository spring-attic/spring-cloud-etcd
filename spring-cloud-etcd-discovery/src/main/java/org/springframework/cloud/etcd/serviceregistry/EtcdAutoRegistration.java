/*
 * Copyright 2015-2019 the original author or authors.
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

package org.springframework.cloud.etcd.serviceregistry;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.cloud.etcd.discovery.EtcdDiscoveryProperties;
import org.springframework.context.ApplicationContext;

/**
 * {@link EtcdRegistration} that populate registration automatically from properties.
 * @author Vladislav Khakin
 *
 * @see Registration
 */
public class EtcdAutoRegistration extends EtcdRegistration {

	public EtcdAutoRegistration(ApplicationContext context,
			EtcdDiscoveryProperties properties) {
		//todo: add properties validation
		super(IdUtils.getDefaultInstanceId(context.getEnvironment()),
				context.getEnvironment().getProperty("spring.application.name",
						"application"),
				properties.getHostname(), properties.getPort(), false);
	}

	public static EtcdAutoRegistration registration(ApplicationContext context,
			EtcdDiscoveryProperties discoveryProperties) {
		return new EtcdAutoRegistration(context, discoveryProperties);
	}
}
