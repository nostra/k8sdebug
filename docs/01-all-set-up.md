# Our setup

We have a prepared Kubernetes cluster with a running java application. 
We have added a set of tools to help us diagnose trouble. In your real life
project, you most likely will have to add these tools  after-the-fact, i.e. 
when you find you need to have a harder look at a problem.

Our resources and setup:

- Nginx entrypoint: [http://localhost:31090/](http://localhost:31090/)
- Our application ##5: [http://k8sdebug.local.gd:31090/](http://k8sdebug.local.gd:31090/)
- Metrics endpoint for our application [http://k8sdebug.local.gd:31090/actuator/prometheus](http://k8sdebug.local.gd:31090/actuator/prometheus)
- Prometheus interface: [Prometheus with memory used query](http://prometheus.local.gd:31090/graph?g0.expr=sum(jvm_memory_max_bytes)&g0.tab=0&g0.display_mode=lines&g0.show_exemplars=0&g0.range_input=5m)
- Grafana: [http://grafana.local.gd:31090/dashboards](http://grafana.local.gd:31090/dashboards)
- K6 dashboard in grafana:  [http://grafana.local.gd:31090/d/ccbb2351-2ae2-462f-ae0e-f2c893ad1028/k6-prometheus?orgId=1](http://grafana.local.gd:31090/d/ccbb2351-2ae2-462f-ae0e-f2c893ad1028/k6-prometheus?orgId=1)

## Dashboard in Grafana

Opening 
[http://grafana.local.gd:31090/d/vJAZ9jwWk/jvm-namespace-centric?orgId=1&refresh=5s](http://grafana.local.gd:31090/d/vJAZ9jwWk/jvm-namespace-centric?orgId=1&refresh=5s)
show you - among other things - that the **heap is quite full**. This is due to
JIB magic. You can adjust some setting by environment variables, which requires you
to understand and this specific product.

Understanding the exact needs of an application, and where and how the memory
is actually used, is taxing, and usually memory get blindly adjusted upwards
until the application works.