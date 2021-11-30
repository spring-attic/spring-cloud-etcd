/*
 * Copyright 2013-2016 the original author or authors.
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

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.etcd.ConditionalOnEtcdEnabled;
import org.springframework.cloud.etcd.discovery.EtcdDiscoveryProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration *
 * Auto-configuration} that auto-register application in Etcd.
 * @author Vladislav Khakin
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(AutoServiceRegistrationProperties.class)
@ConditionalOnEtcdEnabled
@Conditional(EtcdAutoServiceRegistrationAutoConfiguration.OnEtcdRegistrationEnabledCondition.class)
@ConditionalOnMissingBean(AutoServiceRegistration.class)
@AutoConfigureAfter({ AutoServiceRegistrationConfiguration.class, EtcdServiceRegistryAutoConfiguration.class })
public class EtcdAutoServiceRegistrationAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	EtcdAutoServiceRegistration etcdAutoServiceRegistration(
			ServiceRegistry<EtcdRegistration> serviceRegistry,
			AutoServiceRegistrationProperties autoServiceRegistrationProperties,
			EtcdDiscoveryProperties properties, EtcdAutoRegistration autoRegistration) {
		return new EtcdAutoServiceRegistration(serviceRegistry,
				autoServiceRegistrationProperties, properties, autoRegistration);
	}

	@Bean
	@ConditionalOnMissingBean
	EtcdAutoRegistration etcdAutoRegistration(ApplicationContext context,
			EtcdDiscoveryProperties properties) {
		//todo add auto port detection
		return new EtcdAutoRegistration(context, properties);
	}

	protected static class OnEtcdRegistrationEnabledCondition extends AllNestedConditions {

		OnEtcdRegistrationEnabledCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled", matchIfMissing = true)
		static class AutoRegistrationEnabledClass {

		}

		@ConditionalOnProperty(value = "spring.cloud.etcd.service-registry.auto-registration.enabled",
				matchIfMissing = true)
		static class EtcdAutoRegistrationEnabledClass {

		}

		@ConditionalOnProperty(value = "spring.cloud.service-registry.enabled", matchIfMissing = true)
		static class ServiceRegistryEnabledClass {

		}

		@ConditionalOnProperty(value = "spring.cloud.etcd.service-registry.enabled", matchIfMissing = true)
		static class EtcdServiceRegistryEnabledClass {

		}

	}

}
