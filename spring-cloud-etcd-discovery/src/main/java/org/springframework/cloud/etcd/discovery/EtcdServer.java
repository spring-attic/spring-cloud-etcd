package org.springframework.cloud.etcd.discovery;

import com.netflix.loadbalancer.Server;

/**
 * @author Spencer Gibb
 */
public class EtcdServer extends Server {

    private final MetaInfo metaInfo;

	public EtcdServer(final String appName, final String instanceId, String host, String port) {
        super(host, new Integer(port));
        metaInfo = new MetaInfo() {
            @Override
            public String getAppName() {
                return appName;
            }

            @Override
            public String getServerGroup() {
                return null;
            }

            @Override
            public String getServiceIdForDiscovery() {
                return null;
            }

            @Override
            public String getInstanceId() {
                return instanceId;
            }
        };
    }

	@Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }
}
