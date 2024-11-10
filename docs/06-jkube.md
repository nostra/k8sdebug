# JKube

**Insert your laptop into running cluster**

Eclipse JKube can hijack a service in the cluster on your
behalf. You replace the current application in the cluster
with your local laptop. Doc:
[https://eclipse.dev/jkube/docs/kubernetes-maven-plugin/#jkube:remote-dev](https://eclipse.dev/jkube/docs/kubernetes-maven-plugin/#jkube:remote-dev)

You need to suspend flux, to avoid reconciliation which would reset the 
manual configuration. You also need to adjust your Kubernetes context 
so you are in the correct namespace.

Steps:

- Start k8sdebug locally, change output
- Check output from currently running cluster: [http://k8sdebug-app.local.gd:31090/](http://k8sdebug-app.local.gd:31090/)
- Ensure you have a connection to k8s, and that you have the apps namespace
  active. You should see the k8sdebug pod:
```shell
kubectl config set-context --current --namespace=apps
kubectl get pods
```
- Suspend Flux, so it does not reset config:

```shell
flux suspend kustomization k8sdebug
```
- Start k8sdebug locally, change output
- pom.xml has configuration for hijack. Do:
```shell
cd ../spring-app
mvn k8s:remote-dev
```
- Check output again: [http://k8sdebug-app.local.gd:31090/](http://k8sdebug-app.local.gd:31090/)

With the steps above, you should observe output from your locally
running application.

Have a look the pods in the namespace - you have got a new one:
```shell
kubectl get pods
```

When done, stop maven job with CTRL+C, and remember to resume Flux:
```shell
flux resume kustomization k8sdebug
```
