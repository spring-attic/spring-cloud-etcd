package org.springframework.cloud.etcd.discovery;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import lombok.SneakyThrows;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdKeysResponse.EtcdNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Spencer Gibb
 */
public class EtcdServerList extends AbstractServerList<EtcdServer> {

    private EtcdClient etcd;
	private EtcdDiscoveryProperties props;
	private String serviceId;

    public EtcdServerList() {
    }

    public EtcdServerList(EtcdClient etcd, EtcdDiscoveryProperties props, String serviceId) {
        this.etcd = etcd;
		this.props = props;
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

	@SneakyThrows
    private List<EtcdServer> getServers() {
        if (etcd == null) {
            return Collections.emptyList();
        }

		EtcdKeysResponse response = etcd.getDir(props.getDiscoveryPrefix() + "/" + serviceId)
				.send()
				.get();

		List<EtcdNode> nodes = response.node.nodes;

        if (nodes == null || nodes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

		List<EtcdServer> servers = new ArrayList<>();
		for (EtcdNode node : nodes) {
			String[] appInfo = getAppInfo(node.key);
			String appName = appInfo[0];
			String instanceId = appInfo[1];
			String[] strings = node.value.split(":");
			String host = strings[0];
			String port = strings[1];
			EtcdServer server = new EtcdServer(appName, instanceId, host, port);
			servers.add(server);
		}

        return servers;
    }

	private String[] getAppInfo(String key) {
		String serviceNameId = key.replace(props.getDiscoveryPrefix(), "");
		String[] strings = serviceNameId.substring(1).split("/");
		return strings;
	}
}
