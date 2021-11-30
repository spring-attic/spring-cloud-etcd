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

package org.springframework.cloud.etcd.discovery;

import mousio.etcd4j.EtcdClient;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.etcd.ConditionalOnEtcdEnabled;
import org.springframework.cloud.etcd.EtcdAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration *
 *  * Auto-configuration} that configures blocking {@link DiscoveryClient}.
 * @author Spencer Gibb
 * @author Vladislsav Khakin
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
@ConditionalOnEtcdEnabled
@EnableScheduling
@EnableConfigurationProperties
@AutoConfigureBefore({ SimpleDiscoveryClientAutoConfiguration.class, CommonsClientAutoConfiguration.class })
@AutoConfigureAfter({ UtilAutoConfiguration.class, EtcdAutoConfiguration.class })
public class EtcdDiscoveryClientConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public EtcdDiscoveryClient etcdDiscoveryClient(EtcdClient etcd, EtcdDiscoveryProperties properties) {
		return new EtcdDiscoveryClient(etcd, properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public EtcdDiscoveryProperties etcdDiscoveryProperties() {
		return new EtcdDiscoveryProperties();
	}
}
