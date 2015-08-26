/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.etcd.discovery;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.SneakyThrows;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdKeysResponse;

import org.springframework.beans.BeansException;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Spencer Gibb
 */
@Data
public class EtcdDiscoveryClient implements DiscoveryClient, ApplicationContextAware {

	private final EtcdClient etcd;

	private final EtcdLifecycle lifecycle;

	private final EtcdDiscoveryProperties properties;

	private ApplicationContext context;

	@Override
	public String description() {
		return "Spring Cloud etcd Discovery Client";
	}

	@Override
	public ServiceInstance getLocalServiceInstance() {
		return new DefaultServiceInstance(lifecycle.getService().getAppName(),
				properties.getHostname(), lifecycle.getConfiguredPort(), false);
	}

	@Override
	@SneakyThrows
	public List<ServiceInstance> getInstances(final String serviceId) {
		EtcdKeysResponse response = etcd.getDir(lifecycle.getAppKey(serviceId)).send().get();
		List<EtcdKeysResponse.EtcdNode> nodes = response.node.nodes;
		List<ServiceInstance> instances = new ArrayList<>();
		for (EtcdKeysResponse.EtcdNode node : nodes) {
			String[] parts = node.value.split(":");
			instances.add(new DefaultServiceInstance(serviceId, parts[0], Integer.parseInt(parts[1]), false));
		}
		return instances;
	}

	@Override
	@SneakyThrows
	public List<String> getServices() {
		EtcdKeysResponse response = etcd.getDir(properties.getDiscoveryPrefix()).send().get();
		List<EtcdKeysResponse.EtcdNode> nodes = response.node.nodes;
		List<String> services = new ArrayList<>();
		for (EtcdKeysResponse.EtcdNode node : nodes) {
			String serviceId = node.key.replace(properties.getDiscoveryPrefix(), "");
			serviceId = serviceId.substring(1);
			services.add(serviceId);
		}
		return services;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
