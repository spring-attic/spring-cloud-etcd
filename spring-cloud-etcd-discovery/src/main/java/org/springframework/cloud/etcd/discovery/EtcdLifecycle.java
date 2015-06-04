package org.springframework.cloud.etcd.discovery;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import mousio.etcd4j.EtcdClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.AbstractDiscoveryLifecycle;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Spencer Gibb
 */
@CommonsLog
public class EtcdLifecycle extends AbstractDiscoveryLifecycle {

    @Autowired
    private EtcdClient etcd;

    @Autowired
    private EtcdDiscoveryProperties props;

    @Override
    protected void register() {
		Service service = new Service();
		service.setAppName(getAppName());
		service.setId(getContext().getId());
		//TODO: support port = 0 random assignment
		service.setPort(new Integer(getEnvironment().getProperty("server.port", "8080")));

        register(service);
    }

	@Scheduled(initialDelayString = "${spring.cloud.etcd.discovery.heartbeatInterval:25000}", fixedRateString = "${spring.cloud.etcd.discovery.heartbeatInterval:25000}")
	protected void sendHeartbeat() {
		register();
	}

    @Override
	//FIXME: registerManagement
    protected void registerManagement() {
        Service management = new Service();
        management.setId(getManagementServiceId());
        management.setAppName(getManagementServiceName());
        management.setPort(getManagementPort());

        register(management);
    }

	@SneakyThrows
    protected void register(Service service) {
        log.info("Registering service with etcd: "+ service);
		String key = getServiceKey(service.appName, service.getId());
		String value = props.getHostname() + ":" + service.getPort();
		etcd.put(key, value)
				.ttl(props.getTtl())
				.send()
				.get();
    }

	private String getServiceKey(String appName, String serviceId) {
		return props.getDiscoveryPrefix() + "/" + appName + "/" + serviceId;
	}

	@Data
	class Service {
		String appName;
		String id;
		Integer port;
	}

    @Override
    protected int getConfiguredPort() {
        return 0;
    }

    @Override
    protected void setConfiguredPort(int i) {

    }

    @Override
    protected EtcdDiscoveryProperties getConfiguration() {
        return props;
    }

    @Override
    protected void deregister(){
        deregister(getAppName(), getContext().getId());
    }

    @Override
    protected void deregisterManagement() {
        deregister(getAppName(), getManagementServiceName());
    }

	@SneakyThrows
    private void deregister(String appName, String serviceId) {
		etcd.delete(getServiceKey(appName, serviceId)).send();
    }

    @Override
    protected boolean isEnabled() {
        return props.isEnabled();
    }
}
