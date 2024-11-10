# ssh into the pod

```shell
export POD=$(kubectl get pods -o=jsonpath='{range .items..metadata}{.name}{"\n"}{end}'|grep k8sdebug)
kubectl exec -it $POD -- bash
```

# What about distroless images?

- A distroless image contains only the minimal setup for the application.
- Or the running image lacks tools to use for debugging 

See: [https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0](https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0)


## Attach Kubernetes debugger

kubectl debug --help

    ...

    # Create a copy of mypod adding a debug container and attach to it
    kubectl debug mypod -it --image=busybox --copy-to=my-debugger
   
    # Create an interactive debugging session on a node and immediately attach to it.
    # The container will run in the host namespaces and the host's filesystem will be mounted at /host
    kubectl debug node/mynode -it --image=busybox

## Copy the failing container, examining it with ubuntu image:

```shell
export POD=$(kubectl get pods -o=jsonpath='{range .items..metadata}{.name}{"\n"}{end}'|grep k8sdebug)
kubectl debug -it $POD --image bellsoft/liberica-openjdk-debian:23-cds
```

    apt-get install procps    

Find the pid of the java process and try to access the root:

    ls /proc/PID/root

This does not go well.

### DELETE BELOW
Using the same image as the original container:

    kubectl debug -it k8sdebug-8449c77884-t5zbs --image k8sdebug:0.0.1-SNAPSHOT --target k8sdebug

What you can consider making, is an image which is functionally identical, but that
contains a shell for debugging. And possibly other tools: `k8sdebug-dev:0.0.1-SNAPSHOT`

# Distroless images

TODO No success yet with this

The problem is this:
https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0

Dockerfile:

    FROM bellsoft/liberica-openjdk-debian:23
    USER 1000

```shell
docker build -t debug:image .
kind load docker-image debug:image --name k8sdebug
```

Top level doc:
- https://iximiuz.com/en/posts/docker-debug-slim-containers/
- Adops the point of the ephemeral container attachment:
  https://iximiuz.com/en/posts/kubernetes-ephemeral-containers/

Using the same type, to easy loading:
```shell
kubectl debug -it k8sdebug-$HASH --image bellsoft/liberica-openjdk-debian:23 --target k8sdebug
``` 

The following gave me shared file space:

    kubectl debug -it k8sdebug-644c8d5d94-g6g92 --image debug:image



Did not work:
kubectl debug -it k8sdebug-$HASH --share-processes --image bellsoft/liberica-openjdk-debian:23 --copy-to=debug 

