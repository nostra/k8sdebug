# Prometheus Operator and common config

Prometheus is configured using
- [Prometheus Operator](https://prometheus-operator.dev/)
- https://github.com/prometheus-operator/prometheus-operator

```shell
port-forward -n monitoring svc/monitoring-k8s 9090
```

## Install or update
```bash
VERSION="v0.77.1"
curl -L -o operator/prometheus-operator.yaml https://github.com/prometheus-operator/prometheus-operator/releases/download/$VERSION/bundle.yaml
```

Clone https://github.com/prometheus-operator/kube-prometheus/ for basic config of manifests:
```
git@github.com:prometheus-operator/kube-prometheus.git $HOME/git/readonly/kube-prometheus/manifests
```

Used v0.14.0 tag:
```shell
export MANIFESTS=$HOME/git/readonly/kube-prometheus/manifests
ls $MANIFESTS
rm -f alertmanager/*.yaml
cp  $MANIFESTS/alertmanager* alertmanager/.
rm alertmanager/alertmanager-secret.yaml
cd alertmanager
kustomize create --autodetect .
cd ..
rm -f kube-state-metrics/*.yaml
cp  $MANIFESTS/kubeStateMetrics* kube-state-metrics/.
cd kube-state-metrics
kustomize create --autodetect .
cd ..
rm -f kubernetes-controlplane/*.yaml
cp $MANIFESTS/kubernetesControlPlane* kubernetes-controlplane/.
cd kubernetes-controlplane
kustomize create --autodetect .
cd ..
rm -f node-exporter/*.yaml
cp $MANIFESTS/nodeExporter* node-exporter/.
cd node-exporter
kustomize create --autodetect .
cd ..
rm -f grafana/*yaml
cp $MANIFESTS/grafana-* grafana/
cd grafana
git checkout -- kustomization.yaml
git checkout -- grafana-dashboardSources.yaml
git checkout -- grafana-environment.yaml
echo NOT rm grafana-networkPolicy.yaml
echo NOT rm grafana-dashboardDatasources.yaml
echo NOT rm grafana-networkPolicy.yaml
rm grafana-dashboardDefinitions.yaml
cd ..
```



(Separated into two blocks, as IntelliJ got confused otherwise.)

```shell
export MANIFESTS=$HOME/git/readonly/kube-prometheus/manifests
rm -f prometheus/*.yaml
cp $MANIFESTS/prometheus-* prometheus/.
echo "Remove example role binding"
rm manifests/prometheus-roleBindingSpecificNamespaces.yaml
cd prometheus
kustomize create --autodetect .
cd ..
```

Don't think I need to do this: (TODO Verify)
```
git checkout -- prometheus/prometheus-clusterRole.yaml
rm prometheus/prometheus-networkPolicy.yaml
rm prometheus/prometheus-podDisruptionBudget.yaml
rm prometheus/prometheus-roleConfig.yaml
rm prometheus/prometheus-roleBindingConfig.yaml
rm prometheus/prometheus-roleBindingSpecificNamespaces.yaml
rm prometheus/prometheus-roleSpecificNamespaces.yaml
```

