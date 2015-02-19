package org.springframework.cloud.etcd.discovery;

import com.netflix.loadbalancer.Server;

/**
 * @author Spencer Gibb
 */
public class EtcdServer extends Server {

    //private final MetaInfo metaInfo;

	//FIXME: EtcdServer
    public EtcdServer(/*final ServiceNode node*/) {
        super("", 1);
		/*super(node.getNode(), node.getServicePort());
        metaInfo = new MetaInfo() {
            @Override
            public String getAppName() {
                return node.getServiceName();
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
                return node.getServiceID();
            }
        };*/
    }

    @Override
    public MetaInfo getMetaInfo() {
        return null;//metaInfo;
    }
}
