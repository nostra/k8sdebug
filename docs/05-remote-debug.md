# Connect a debugger

Attaching a debugger to a running application. Prerequisite is
that the application is configured with the proper jvm arguments: ##6

    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

In addition, you need to add "Remote vm debugging" to the IntelliJ debug
configuration.

```shell
export POD=$(podhash k8sdebug)
echo "Pod: $POD"
kubectl port-forward $POD 5005:5005
```

- Create a breakpoint the controller (##2), and observe the pause. 
- What happens to the running k6 tests?
      - Pro-tip: Remove probes in order to avoid Kubernetes restart of pod when sleeping on breakpoint.

## The function podhash

The function podhash is used to find a pod + hash for a given name:
```
function podhash {
    if [[ "x" != "x$1" ]] ; then
        kubectl get pods -o=jsonpath='{range .items..metadata}{.name}{"\n"}{end}'|grep $1|head -1
    fi
}
```