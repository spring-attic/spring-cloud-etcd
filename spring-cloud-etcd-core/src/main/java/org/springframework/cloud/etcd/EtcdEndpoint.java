package org.springframework.cloud.etcd;

import mousio.etcd4j.EtcdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties(prefix = "endpoints.etcd", ignoreUnknownFields = false)
public class EtcdEndpoint extends AbstractEndpoint<EtcdEndpoint.Data> {

	private EtcdClient etcd;

	@Autowired
    public EtcdEndpoint(EtcdClient etcd) {
        super("etcd", false, true);
		this.etcd = etcd;
	}

    @Override
    public Data invoke() {
        Data data = new Data();
		data.setVersion(etcd.getVersion());
        return data;
    }

    @lombok.Data
    public static class Data {
		private String version;
    }
}
