package org.springframework.cloud.etcd.discovery;

import lombok.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties("spring.cloud.etcd.discovery")
@Data
@CommonsLog
public class EtcdDiscoveryProperties {
	private boolean enabled = true;

	private String discoveryPrefix = "/spring/cloud/discovery";

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private HostInfo hostInfo = initHostInfo();

	private String ipAddress = this.hostInfo.getIpAddress();

	private String hostname = this.hostInfo.getHostname();

	private boolean preferIpAddress = false;

	private int ttl = 30;

	private int heartbeatInterval = 25000;

	public String getHostname() {
		return this.preferIpAddress ? this.ipAddress : this.hostname;
	}

	@SneakyThrows
	private HostInfo initHostInfo() {
		return new HostInfo(InetAddress.getLocalHost().getHostAddress(),
			InetAddress.getLocalHost().getHostName());
	}

	@Data
	private class HostInfo {
		private final String ipAddress;
		private final String hostname;
	}
}
