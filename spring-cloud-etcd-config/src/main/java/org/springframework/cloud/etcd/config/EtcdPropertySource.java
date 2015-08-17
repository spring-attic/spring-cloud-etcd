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
package org.springframework.cloud.etcd.config;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Luca Burgazzoli
 */
public class EtcdPropertySource extends EnumerablePropertySource<EtcdClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EtcdPropertySource.class);

    private final Map<String, String> properties;
    private final String prefix;

    public EtcdPropertySource(String root, EtcdClient source) {
        super(root, source);
        this.properties = new HashMap<>();
        this.prefix = root.startsWith(EtcdConstants.PATH_SEPARATOR)
            ? root + EtcdConstants.PATH_SEPARATOR
            : EtcdConstants.PATH_SEPARATOR + root +  EtcdConstants.PATH_SEPARATOR;
    }

    public void init() {
        try {
            final EtcdKeysResponse response = getSource().getDir(getName())
                .recursive()
                .timeout(1, TimeUnit.SECONDS)
                .send()
                .get();

            if(response.node != null) {
                process(response.node);
            }
        } catch(Exception e) {
            LOGGER.warn("", e);
        }
    }

    @Override
    public String[] getPropertyNames() {
        return properties.keySet().toArray(new String[0]);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    // *************************************************************************
    //
    // *************************************************************************

    private void process(final EtcdKeysResponse.EtcdNode root) {
        if(!StringUtils.isEmpty(root.value)) {
            final String key = root.key.substring(this.prefix.length());

            properties.put(
                key.replace(EtcdConstants.PATH_SEPARATOR, EtcdConstants.PROPERTIES_SEPARATOR),
                root.value
            );
        }

        if(!CollectionUtils.isEmpty(root.nodes)) {
            for(EtcdKeysResponse.EtcdNode node : root.nodes) {
                process(node);
            }
        }
    }
}

