# K6

K6 could have been named [Kapablo](https://community.grafana.com/t/what-is-the-story-behind-the-name-of-k6/98516).

- [https://grafana.com/docs/k6/latest/](https://grafana.com/docs/k6/latest/)
- [https://github.com/grafana/k6-operator/tree/main](https://github.com/grafana/k6-operator/tree/main)

```shell
kubectl create -k ../clusters/kind/apps/k6test/
```

## Different tests

- Smoke test
- Average Load test
- Stress test
- Soak test ##4

We are focusing on the soak test in the following.

We use Prometheus to examine the metrics. For this to work, we need
to configure Prometheus to accept remote write:
[https://prometheus.io/docs/prometheus/latest/feature_flags/#remote-write-receiver](https://prometheus.io/docs/prometheus/latest/feature_flags/#remote-write-receiver)
