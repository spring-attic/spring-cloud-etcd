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

import mousio.etcd4j.EtcdClient;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration;
import org.springframework.cloud.etcd.ConditionalOnEtcdEnabled;
import org.springframework.cloud.etcd.discovery.EtcdDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} that configures Etcd {@link ServiceRegistry}.
 * @author Vladislav Khakin
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnEtcdEnabled
@AutoConfigureBefore(ServiceRegistryAutoConfiguration.class)
@Conditional(EtcdServiceRegistryAutoConfiguration.OnEtcdRegistrationEnabledCondition.class)
public class EtcdServiceRegistryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	EtcdServiceRegistry etcdServiceRegistry(EtcdClient etcd,
			EtcdDiscoveryProperties properties) {
		return new EtcdServiceRegistry(etcd, properties);
	}

	@Bean
	@ConditionalOnMissingBean
	EtcdDiscoveryProperties properties() {
		return new EtcdDiscoveryProperties();
	}

	static class OnEtcdRegistrationEnabledCondition extends AllNestedConditions {

		OnEtcdRegistrationEnabledCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(value = "spring.cloud.service-registry.enabled", matchIfMissing = true)
		static class ServiceRegistryEnabledClass {

		}

		@ConditionalOnProperty(value = "spring.cloud.etcd.service-registry.enabled", matchIfMissing = true)
		static class EtcdServiceRegistryEnabledClass {

		}

	}
}
