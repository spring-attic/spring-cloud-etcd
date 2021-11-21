#!/bin/bash

#!/usr/bin/env bash

ETCD_URL="https://github.com/etcd-io/etcd/releases/download"
ETCD_VER="v3.5.1"
ETCD_ARC="darwin-amd64"

# cleanup
rm -rf etcd-${ETCD_VER}-${ETCD_ARC}.tar.gz
rm -rf etcd-${ETCD_VER}-${ETCD_ARC}
rm -rf etcd
rm -rf default.etcd

mkdir etcd-dist

# install etcd
curl -L ${ETCD_URL}/${ETCD_VER}/etcd-${ETCD_VER}-${ETCD_ARC}.tar.gz \
    | tar xzf - \
        --directory ./etcd-dist \
        --strip-components=1

# check
./etcd-dist/etcd --version
./etcd-dist/etcdctl version --version
./etcd-dist/etcdutl version --version

