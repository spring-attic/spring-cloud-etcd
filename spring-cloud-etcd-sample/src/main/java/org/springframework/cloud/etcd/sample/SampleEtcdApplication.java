package org.springframework.cloud.etcd.sample;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Spencer Gibb
 */
@SpringCloudApplication
@RestController
@CommonsLog
public class SampleEtcdApplication {

    public static final String CLIENT_NAME = "testEtcdApp";

    @Autowired
    LoadBalancerClient loadBalancer;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    Environment env;

    @Autowired(required = false)
    RelaxedPropertyResolver resolver;

    @RequestMapping("/me")
    public ServiceInstance me() {
        return discoveryClient.getLocalServiceInstance();
    }

    @RequestMapping("/")
    public ServiceInstance lb() {
        return loadBalancer.choose(CLIENT_NAME);
    }

    @RequestMapping("/myenv")
    public String env(@RequestParam("prop") String prop) {
        String property = new RelaxedPropertyResolver(env).getProperty(prop, "Not Found");
        return property;
    }

    public static void main(String[] args) {
        SpringApplication.run(SampleEtcdApplication.class, args);
    }
}
