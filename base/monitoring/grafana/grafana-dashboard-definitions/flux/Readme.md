Grafana dashboards:
- https://github.com/fluxcd/flux2-monitoring-example/tree/main/monitoring/configs/dashboards

Update by:
```
git clone git@github.com:fluxcd/flux2-monitoring-example.git
cp -i flux2-monitoring-example/monitoring/configs/dashboards/* $BASE/grafana/grafana-dashboard-definitions/flux/.
```