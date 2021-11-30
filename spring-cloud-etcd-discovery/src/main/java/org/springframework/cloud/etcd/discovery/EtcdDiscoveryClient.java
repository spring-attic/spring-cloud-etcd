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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * Etcd version of {@link DiscoveryClient}.
 * @author Spencer Gibb
 * @author Vladislav Khakin
 */
public class EtcdDiscoveryClient implements DiscoveryClient {

	private final EtcdClient etcd;
	private final EtcdDiscoveryProperties properties;

	public EtcdDiscoveryClient(EtcdClient etcd, EtcdDiscoveryProperties properties) {
		this.etcd = etcd;
		this.properties = properties;
	}

	@Override
	public String description() {
		return "Spring Cloud Etcd Discovery Client";
	}

	@Override
	public List<ServiceInstance> getInstances(final String serviceId) {
		List<ServiceInstance> instances = null;
		try {
//			todo: extract service key building to utils
			EtcdKeysResponse response = etcd
					.getDir(properties.getDiscoveryPrefix() + "/" + serviceId).send()
					.get();
			List<EtcdKeysResponse.EtcdNode> nodes = response.node.nodes;
			instances = new ArrayList<>();
			for (EtcdKeysResponse.EtcdNode node : nodes) {
				String[] parts = node.value.split(":");
				instances.add(new DefaultServiceInstance("", serviceId, parts[0],
						Integer.parseInt(parts[1]), false));
			}
		}
		catch (IOException | TimeoutException | EtcdException | EtcdAuthenticationException e) {
			rethrowRuntimeException(e);
		}
		return instances;
	}

	@Override
	public List<String> getServices() {
		List<String> services = null;
		try {
			EtcdKeysResponse response = etcd.getDir(properties.getDiscoveryPrefix())
					.send().get();
			List<EtcdKeysResponse.EtcdNode> nodes = response.node.nodes;
			services = new ArrayList<>();
			for (EtcdKeysResponse.EtcdNode node : nodes) {
				String serviceId = node.key.replace(properties.getDiscoveryPrefix(), "");
				serviceId = serviceId.substring(1);
				services.add(serviceId);
			}
		}
		catch (IOException | EtcdException | TimeoutException | EtcdAuthenticationException e) {
			rethrowRuntimeException(e);
		}
		return services;
	}
}
