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

package org.springframework.cloud.etcd;

import mousio.etcd4j.EtcdClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties(prefix = "endpoints.etcd", ignoreUnknownFields = false)
public class EtcdEndpoint extends AbstractEndpoint<EtcdEndpoint.Data> {

	private EtcdClient etcd;

	@Autowired
	public EtcdEndpoint(EtcdClient etcd) {
		super("etcd", false, true);
		this.etcd = etcd;
	}

	@Override
	public Data invoke() {
		Data data = new Data();
		data.setVersion(etcd.getVersion());
		return data;
	}

	@lombok.Data
	public static class Data {
		private String version;
	}
}
