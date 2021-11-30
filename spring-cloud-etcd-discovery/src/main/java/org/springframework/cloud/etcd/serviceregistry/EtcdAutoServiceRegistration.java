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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.etcd.discovery.EtcdDiscoveryProperties;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Etcd {@link AbstractAutoServiceRegistration} that uses {@link EtcdServiceRegistry} to
 * register and de-register instances.
 * @author Vladislav Khakin
 */
public class EtcdAutoServiceRegistration
		extends AbstractAutoServiceRegistration<EtcdRegistration> {

	private static final Log log = LogFactory.getLog(EtcdAutoServiceRegistration.class);

	private final EtcdDiscoveryProperties properties;
	private final EtcdAutoRegistration registration;

	public EtcdAutoServiceRegistration(ServiceRegistry<EtcdRegistration> serviceRegistry,
			AutoServiceRegistrationProperties autoRegistrationProperties,
			EtcdDiscoveryProperties discoveryProperties,
			EtcdAutoRegistration registration) {
		super(serviceRegistry, autoRegistrationProperties);
		this.properties = discoveryProperties;
		this.registration = registration;
	}

	@Scheduled(initialDelayString = "${spring.cloud.etcd.discovery.heartbeatInterval:25000}", fixedRateString = "${spring.cloud.etcd.discovery.heartbeatInterval:25000}")
	protected void sendHeartbeat() {
		register();
	}

	@Override
	protected EtcdRegistration getRegistration() {
		// TODO: process random port generation for web server
		return registration;
	}

	@Override
	protected EtcdRegistration getManagementRegistration() {
		return null;
	}

	@Override
	protected Object getConfiguration() {
		return properties;
	}

	@Override
	protected boolean isEnabled() {
		return properties.isEnabled();
	}

	@Override
	protected void register() {
		if (!properties.isRegister()) {
			log.debug("Etcd Registration disabled.");
			return;
		}
		super.register();
	}

	@Override
	protected void deregister() {
		if (!this.properties.isRegister() || !this.properties.isDeregister()) {
			return;
		}
		super.deregister();
	}
}
