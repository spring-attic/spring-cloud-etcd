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

package org.springframework.cloud.etcd;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdVersionResponse;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.util.Assert;

//TODO: do we actually need specific endpoint? is health indicator enough?
/**
 * {@link Endpoint @Endpoint} to expose Etcd details.
 * @author Spencer Gibb
 * @author Vladislav Khakin
 */
@Endpoint(id = "etcd")
public class EtcdEndpoint {

	private final EtcdClient etcd;

	public EtcdEndpoint(EtcdClient etcd) {
		Assert.notNull(etcd, "Etcd must not be null");
		this.etcd = etcd;
	}

	@ReadOperation
	public EtcdData etcdData() {
		EtcdVersionResponse version = etcd.version();
		return new EtcdData(version.cluster, version.server);
	}

	/**
	 * Etcd details, primarily intended for serialization to JSON.
	 */
	public static class EtcdData {
		private final String clusterVersion;
		private final String serverVersion;

		public EtcdData(String clusterVersion, String serverVersion) {
			this.clusterVersion = clusterVersion;
			this.serverVersion = serverVersion;
		}

		public String getServerVersion() {
			return serverVersion;
		}

		public String getClusterVersion() {
			return clusterVersion;
		}
	}
}
