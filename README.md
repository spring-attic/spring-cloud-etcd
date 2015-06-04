## Spring Cloud Consul

Preview of Spring Cloud Consul implementation

### Running the sample

1. [Install etcd](https://github.com/coreos/etcd/releases)
2. Run `./run_etcdl.sh` found in the root of this project
3. verify etcd is running by visiting [http://localhost:4001/version](http://localhost:4001/version)
4. run `mvn package`
5. TODO: run `java -jar spring-etcd-consul-sample/target/spring-cloud-etcd-sample-1.0.0.BUILD-SNAPSHOT.jar`
6. TODO: visit [http://localhost:8080](http://localhost:8080), verify that `{"serviceId":"<yourhost>:8080","host":"<yourhost>","port":8080}` results
5. TODO: run `java -jar spring-cloud-etcd-sample/target/spring-cloud-etcd-sample-1.0.0.BUILD-SNAPSHOT.jar --server.port=8081`
6. TODO: visit [http://localhost:8080](http://localhost:8080) again, verify that `{"serviceId":"<yourhost>:8081","host":"<yourhost>","port":8081}` eventually shows up in the results in a round robbin fashion (may take a minute or so).
