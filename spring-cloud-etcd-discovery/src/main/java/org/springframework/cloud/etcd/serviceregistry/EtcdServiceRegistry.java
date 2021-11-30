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

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.etcd.discovery.EtcdDiscoveryProperties;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;


/**
 * {@link ServiceRegistry} that register and deregister services in Etcd.
 * @author Vladislav Khakin
 */
public class EtcdServiceRegistry implements ServiceRegistry<EtcdRegistration> {

	private static final Log log = LogFactory.getLog(EtcdServiceRegistry.class);

	private final EtcdClient etcd;
	private final EtcdDiscoveryProperties properties;

	public EtcdServiceRegistry(EtcdClient etcd, EtcdDiscoveryProperties properties) {
		this.etcd = etcd;
		this.properties = properties;
	}

	@Override
	public void register(EtcdRegistration registration) {
		try {
			log.info("Registering service with etcd: " + registration);
			String key = getServiceKey(registration);
			// TODO: what should be serialized about the service?
			String value = registration.getHost() + ":" + registration.getPort();
			etcd.put(key, value).timeout(2, TimeUnit.SECONDS).ttl(properties.getTtl())
					.send().get();
		}
		catch (IOException | TimeoutException | EtcdException
				| EtcdAuthenticationException e) {
			// todo: add fail-fast
			rethrowRuntimeException(e);
		}
	}

	@Override
	public void deregister(EtcdRegistration registration) {
		try {
			etcd.delete(getServiceKey(registration)).send();
		}
		catch (IOException e) {
			rethrowRuntimeException(e);
		}
	}

	@Override
	public void close() {
		try {
			etcd.close();
		}
		catch (IOException e) {
			rethrowRuntimeException(e);
		}
	}

	@Override
	public void setStatus(EtcdRegistration registration, String status) {
		// todo: add metadata to payload
	}

	@Override
	public <T> T getStatus(EtcdRegistration registration) {
		return null;
	}

	private String getServiceKey(EtcdRegistration registration) {
		return getAppKey(registration.getServiceId()) + "/"
				+ registration.getInstanceId();
	}

	private String getAppKey(String appName) {
		return properties.getDiscoveryPrefix() + "/" + appName;
	}
}
