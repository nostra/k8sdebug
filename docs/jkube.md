# JKube

# Insert your laptop into running cluster

Eclipse JKube can hijack a service in the cluster on your
behalf. Doc:
https://eclipse.dev/jkube/docs/kubernetes-maven-plugin/#jkube:remote-dev

You need to suspend flux, so the configuration does not get reset. You
also need to configure Kubernetes so you are in the correct namespace.

Steps:
- Check output from currently running cluster: http://k8sdebug-app.local.gd:31090/
- Ensure you have a connection to k8s, and that you have the apps namespace
  active. You should see the k8sdebug pod:
```
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
- Check output again: http://k8sdebug-app.local.gd:31090/

With the steps above, you should observe output from your locally
running application.

When done, stop maven job with CTRL+C, and remember to resume Flux:
```
flux resume kustomization k8sdebug
```
