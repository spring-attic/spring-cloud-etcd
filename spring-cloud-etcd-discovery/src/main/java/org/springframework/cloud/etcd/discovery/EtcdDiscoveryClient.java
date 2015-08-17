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
import java.util.Collections;
import java.util.List;

import mousio.etcd4j.EtcdClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * @author Spencer Gibb
 */
public class EtcdDiscoveryClient implements DiscoveryClient {

	@Autowired
	ApplicationContext context;

	@Autowired
	EtcdClient etcd;

	@Override
	public String description() {
		return "Spring Cloud etcd Discovery Client";
	}

	@Override
	// FIXME: getLocalServiceInstance
	public ServiceInstance getLocalServiceInstance() {
        /* Map<String, Service> services = agentClient.getServices();
        Service service = services.get(context.getId());
        if (service == null) {
            throw new IllegalStateException("Unable to locate service in consul agent: "+context.getId());
        }
        String host = "localhost";
        Map<String, Object> self = agentClient.getSelf();
        Map<String, Object> member = (Map<String, Object>) self.get("Member");
        if (member != null) {
            if (member.containsKey("Name")) {
                host = (String) member.get("Name");
            }
        }
        return new DefaultServiceInstance(service.getId(), host, service.getPort(), false);*/
		return new DefaultServiceInstance(null, null, 0, false);
	}

	@Override
	// FIXME: getInstances
	public List<ServiceInstance> getInstances(final String serviceId) {
		/*
		 * List<ServiceNode> nodes = catalogClient.getServiceNodes(serviceId);
		 * List<ServiceInstance> instances = new ArrayList<>(); for (ServiceNode node :
		 * nodes) { instances.add(new DefaultServiceInstance(serviceId, node.getNode(),
		 * node.getServicePort(), false)); }
		 * 
		 * return instances;
		 */
		return Collections.emptyList();
	}

	@Override
	// FIXME: getServices
	public List<String> getServices() {
		return new ArrayList<>();// catalogClient.getServices().keySet());
	}
}
