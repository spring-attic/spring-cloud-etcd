/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ReflectionUtils;

/**
 * Defines configuration for Etcd service discovery and registration.
 *
 * @author Spencer Gibb
 * @author Vladislav Khakin
 */
@ConfigurationProperties(EtcdDiscoveryProperties.PREFIX)
public class EtcdDiscoveryProperties {

	private static final Log log = LogFactory.getLog(EtcdDiscoveryProperties.class);
	/**
	 * Etcd discovery properties prefix.
	 */
	public static final String PREFIX = "spring.cloud.etcd.discovery";

	/** Is service discovery enabled? */
	private boolean enabled = true;

	/** Register as a service in Etcd. */
	private boolean register = true;

	/** Disable automatic de-registration of service in Etcd. */
	private boolean deregister = true;

	/** Discovery prefix. */
	private String discoveryPrefix = "/spring/cloud/discovery";

	/** Port to register the service under (defaults to listening port). */
	private Integer port;

	private HostInfo hostInfo = initHostInfo();

	private String ipAddress = this.hostInfo.getIpAddress();

	private String hostname = this.hostInfo.getHostname();

	private boolean preferIpAddress = false;

	private int ttl = 30;

	private int heartbeatInterval = 25000;

	public String getHostname() {
		return this.preferIpAddress ? this.ipAddress : this.hostname;
	}

	private HostInfo initHostInfo() {
		InetAddress ipAddress = getIpAddress();
		return new HostInfo(ipAddress.getHostAddress(), ipAddress.getHostName());
	}

	public static InetAddress getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> enumNic = NetworkInterface
					.getNetworkInterfaces(); enumNic.hasMoreElements();) {
				NetworkInterface ifc = enumNic.nextElement();
				if (ifc.isUp()) {
					for (Enumeration<InetAddress> enumAddr = ifc
							.getInetAddresses(); enumAddr.hasMoreElements();) {
						InetAddress address = enumAddr.nextElement();
						if (address instanceof Inet4Address
								&& !address.isLoopbackAddress()) {
							return address;
						}
					}
				}
			}
			return InetAddress.getLocalHost();
		}
		catch (UnknownHostException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
		catch (IOException e) {
			log.warn("Unable to find non-loopback address", e);
			return null;
		}
	}

	public static Log getLog() {
		return log;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRegister() {
		return register;
	}

	public void setRegister(boolean register) {
		this.register = register;
	}

	public boolean isDeregister() {
		return this.deregister;
	}

	public void setDeregister(boolean deregister) {
		this.deregister = deregister;
	}

	public String getDiscoveryPrefix() {
		return discoveryPrefix;
	}

	public void setDiscoveryPrefix(String discoveryPrefix) {
		this.discoveryPrefix = discoveryPrefix;
	}

	private HostInfo getHostInfo() {
		return hostInfo;
	}

	private void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isPreferIpAddress() {
		return preferIpAddress;
	}

	public void setPreferIpAddress(boolean preferIpAddress) {
		this.preferIpAddress = preferIpAddress;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getHeartbeatInterval() {
		return heartbeatInterval;
	}

	public void setHeartbeatInterval(int heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("enabled", enabled)
				.append("register", register).append("deregister", deregister)
				.append("discoveryPrefix", discoveryPrefix).append("port", port)
				.append("hostInfo", hostInfo).append("ipAddress", ipAddress)
				.append("hostname", hostname).append("preferIpAddress", preferIpAddress)
				.append("ttl", ttl).append("heartbeatInterval", heartbeatInterval)
				.toString();
	}

	private class HostInfo {

		private final String ipAddress;

		private final String hostname;

		HostInfo(String ipAddress, String hostname) {
			this.ipAddress = ipAddress;
			this.hostname = hostname;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public String getHostname() {
			return hostname;
		}

		@Override
		public String toString() {
			return String.format("HostInfo{ipAddress='%s', hostname='%s'}", ipAddress,
					hostname);
		}

	}
}
