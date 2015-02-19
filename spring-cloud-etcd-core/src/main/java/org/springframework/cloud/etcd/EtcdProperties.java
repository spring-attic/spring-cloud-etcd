package org.springframework.cloud.etcd;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties("spring.cloud.etcd")
@Data
public class EtcdProperties {
    @NotNull
    private List<URI> uris = Arrays.asList(URI.create("http://localhost:4001"));

    private boolean enabled = true;
}
