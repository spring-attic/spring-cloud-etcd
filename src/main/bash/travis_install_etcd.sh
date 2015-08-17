#!/bin/bash

ETCD_VER="v2.1.1"
ETCD_ARC="linux-amd64"

# cleanup
rm -rf etcd-${ETCD_VER}-${ETCD_ARC}.tar.gz
rm -rf etcd-${ETCD_VER}-${ETCD_ARC}
rm -rf etcd

mkdir etcd-dist

# install etcd
curl -L  https://github.com/coreos/etcd/releases/download/${ETCD_VER}/etcd-${ETCD_VER}-${ETCD_ARC}.tar.gz | tar xzf - --directory ./etcd-dist --strip-components=1


# check
./etcd-dist/etcd --version
