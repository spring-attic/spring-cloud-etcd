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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
		InetAddress ipAddress = getIpAddress();
		return new HostInfo(ipAddress.getHostAddress(), ipAddress.getHostName());
	}

	@SneakyThrows
	public static InetAddress getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> enumNic = NetworkInterface.getNetworkInterfaces();
				 enumNic.hasMoreElements(); ) {
				NetworkInterface ifc = enumNic.nextElement();
				if (ifc.isUp()) {
					for (Enumeration<InetAddress> enumAddr = ifc.getInetAddresses();
						 enumAddr.hasMoreElements(); ) {
						InetAddress address = enumAddr.nextElement();
						if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
							return address;
						}
					}
				}
			}
		} catch (IOException e) {
			log.warn("Unable to find non-loopback address", e);
		}
		return InetAddress.getLocalHost();
	}

	@Data
	private class HostInfo {
		private final String ipAddress;
		private final String hostname;
	}
}
