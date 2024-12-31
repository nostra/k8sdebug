# Setup of pg-cluster

- https://cloudnative-pg.io/documentation/1.25/quickstart/#part-3-deploy-a-postgresql-cluster

- https://cloudnative-pg.io/documentation/1.25/quickstart/#part-4-monitor-clusters-with-prometheus-and-grafana

## Monitoring

```shell
curl -L -o prometheusrule.yaml https://raw.githubusercontent.com/cloudnative-pg/cloudnative-pg/main/docs/src/samples/monitoring/prometheusrule.yaml
```

## Connecting as user

```shell
kubectl run psql-shell --rm -i --tty --image postgres:17-alpine -n cnpg-cluster -- psql "postgresql://apicurio:apicurio-password@pg-cluster-rw.cnpg-cluster:5432/apicuriodb"
```

## Use of cnpg

```
kubectl-cnpg status pg-cluster
kubectl-cnpg psql pg-cluster 
```
