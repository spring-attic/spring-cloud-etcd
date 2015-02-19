package org.springframework.cloud.etcd.discovery;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import mousio.etcd4j.EtcdClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Spencer Gibb
 */
public class EtcdServerList extends AbstractServerList<EtcdServer> {

    private EtcdClient etcd;

    private String serviceId;

    public EtcdServerList() {
    }

    public EtcdServerList(EtcdClient etcd, String serviceId) {
        this.etcd = etcd;
        this.serviceId = serviceId;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
		this.serviceId = clientConfig.getClientName();
    }

    @Override
    public List<EtcdServer> getInitialListOfServers() {
        return getServers();
    }

    @Override
    public List<EtcdServer> getUpdatedListOfServers() {
        return getServers();
    }

	//FIXME: getServers
    private List<EtcdServer> getServers() {
        //if (client == null) {
            return Collections.emptyList();
        /*}
        List<ServiceNode> nodes = client.getServiceNodes(serviceId);
        if (nodes == null || nodes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

		List<EtcdServer> servers = new ArrayList<>();
		for (ServiceNode node : nodes) {
			EtcdServer server = new EtcdServer(node);
			servers.add(server);
		}

        return servers;*/
    }
}
