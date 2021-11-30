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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mousio.etcd4j.EtcdClient;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

/**
 * Wrapper annotation to enable Etcd.
 * @author Vladislav Khakin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Conditional(ConditionalOnEtcdEnabled.OnEtcdEnabledCondition.class)
public @interface ConditionalOnEtcdEnabled {

	/**
	 * Verifies multiple conditions to see if Etcd should be enabled.
	 */
	class OnEtcdEnabledCondition extends AllNestedConditions {

		OnEtcdEnabledCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		/**
		 * Etcd property is enabled.
		 */
		@ConditionalOnProperty(value = "spring.cloud.etcd.enabled", matchIfMissing = true)
		static class FoundProperty {

		}

		/**
		 * Etcd client class found.
		 */
		@ConditionalOnClass(EtcdClient.class)
		static class FoundClass {

		}

	}
}
