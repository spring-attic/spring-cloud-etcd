package org.springframework.cloud.etcd.discovery;

import mousio.etcd4j.EtcdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	//FIXME: getLocalServiceInstance
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
	//FIXME: getInstances
    public List<ServiceInstance> getInstances(final String serviceId) {
        /*List<ServiceNode> nodes = catalogClient.getServiceNodes(serviceId);
		List<ServiceInstance> instances = new ArrayList<>();
		for (ServiceNode node : nodes) {
            instances.add(new DefaultServiceInstance(serviceId, node.getNode(), node.getServicePort(), false));
		}

        return instances;*/
		return Collections.emptyList();
    }

    @Override
	//FIXME: getServices
    public List<String> getServices() {
        return new ArrayList<>();//catalogClient.getServices().keySet());
    }
}
