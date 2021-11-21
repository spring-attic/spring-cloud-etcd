#!/usr/bin/env bash

./etcd-dist/etcd --name 'spring-cloud-etcd' --data-dir /tmp/etcd/spring-cloud-etcd &

sleep 2

#./etcd-dist/etcdctl set /test/messages 'data'
#./etcd-dist/etcdctl --endpoints=localhost:2379 put /test/messages bar
