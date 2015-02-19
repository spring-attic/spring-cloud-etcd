package org.springframework.cloud.etcd;

import mousio.etcd4j.EtcdClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableConfigurationProperties
public class EtcdAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public EtcdClient etcdClient() {
		//TODO: support ssl
		return new EtcdClient(etcdProperties().getUris().toArray(new URI[]{}));
	}

    @Bean
    @ConditionalOnMissingBean
    public EtcdProperties etcdProperties() {
        return new EtcdProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public EtcdEndpoint etcdEndpoint() {
        return new EtcdEndpoint(etcdClient());
    }

    @Bean
    @ConditionalOnMissingBean
    public EtcdHealthIndicator etcdHealthIndicator() {
        return new EtcdHealthIndicator();
    }
}
