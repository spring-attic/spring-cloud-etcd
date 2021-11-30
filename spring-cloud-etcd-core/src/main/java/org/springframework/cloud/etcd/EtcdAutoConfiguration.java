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

package org.springframework.cloud.etcd;

import java.net.URI;

import mousio.etcd4j.EtcdClient;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * {@link EnableAutoConfiguration Auto-configuration} for Etcd Client.
 * @author Spencer Gibb
 * @author Vladislav Khakin
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnEtcdEnabled
public class EtcdAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public EtcdClient etcdClient(EtcdProperties properties) {
		// TODO: support ssl
		// TODO: support authentication
		return new EtcdClient(properties.getUris().toArray(new URI[] {}));
	}

	@Bean
	@ConditionalOnMissingBean
	public EtcdProperties etcdProperties() {
		return new EtcdProperties();
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(Endpoint.class)
	protected static class EtcdHealthConfig {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnAvailableEndpoint
		public EtcdEndpoint etcdEndpoint(EtcdClient etcd) {
			return new EtcdEndpoint(etcd);
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnEnabledHealthIndicator("etcd")
		public EtcdHealthIndicator etcdHealthIndicator(EtcdClient etcd) {
			return new EtcdHealthIndicator(etcd);
		}

	}
}
