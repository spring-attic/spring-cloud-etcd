/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
