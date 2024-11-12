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


### Attach an ephemeral container to the original

Using the same image as the original container:

    kubectl debug -it k8sdebug-$HASH --image k8sdebug:manual --target k8sdebug -- bash

Find the pid (by looking at `/proc`) and examine the directory structure:

    ls /proc/6/root/workspace/org/springframework/

Jcmd does not work with this, unfortunately. 

What you can consider making, is an image which is functionally identical, but that
contains a shell for debugging. And possibly other tools: `k8sdebug-dev:...`

# Distroless images

The problem is this:
https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0

Dockerfile:

    FROM bellsoft/liberica-openjdk-debian:23
    USER 1000

```shell
docker build -t debug:image .
kind load docker-image debug:image --name k8sdebug
```

Using the same type, to easy loading:
```shell
kubectl debug -it k8sdebug-$HASH --image bellsoft/liberica-openjdk-debian:23 --target k8sdebug
``` 

Create a copy of the pods with problems, and attach to it:

    kubectl debug -it k8sdebug-$HASH --image k8sdebug:manual --share-processes --copy-to=debug -- bash

The attached debug container does not have probes, so it won't get restarted.