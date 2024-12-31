# Setup of pg-cluster

- https://cloudnative-pg.io/documentation/1.25/quickstart/#part-3-deploy-a-postgresql-cluster

- https://cloudnative-pg.io/documentation/1.25/quickstart/#part-4-monitor-clusters-with-prometheus-and-grafana

## Monitoring

```shell
curl -L -o prometheusrule.yaml https://raw.githubusercontent.com/cloudnative-pg/cloudnative-pg/main/docs/src/samples/monitoring/prometheusrule.yaml

```

```shell
helm template \
  -f https://raw.githubusercontent.com/cloudnative-pg/cloudnative-pg/main/docs/src/samples/monitoring/kube-stack-config.yaml \
  prometheus-community \
  prometheus-community/kube-prometheus-stack > 
```
