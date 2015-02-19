package org.springframework.cloud.etcd;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

/**
 * @author Spencer Gibb
 */
public class EtcdHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            builder.up();
        } catch (Exception e) {
            builder.down(e);
        }
    }
}
