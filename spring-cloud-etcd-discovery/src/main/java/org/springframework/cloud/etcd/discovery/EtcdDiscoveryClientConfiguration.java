package org.springframework.cloud.etcd.discovery;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties
public class EtcdDiscoveryClientConfiguration {
    @Bean
    public EtcdLifecycle etcdLifecycle() {
        return new EtcdLifecycle();
    }

    @Bean
    public EtcdDiscoveryClient etcdDiscoveryClient() {
        return new EtcdDiscoveryClient();
    }

	@Bean
	public EtcdDiscoveryProperties etcdDiscoveryProperties() {
		return new EtcdDiscoveryProperties();
	}
}
