# ssh into the pod

    kubect exec -it k8sdebug-$HASH -- bash

# What about distroless images?

- A distroless image contains only the minimal setup for the application.
- Or the running image lacks tools to use for debugging 

## Attach Kubernetes debugger

kubectl debug --help

    ...

    # Create a copy of mypod adding a debug container and attach to it
    kubectl debug mypod -it --image=busybox --copy-to=my-debugger
   
    # Create an interactive debugging session on a node and immediately attach to it.
    # The container will run in the host namespaces and the host's filesystem will be mounted at /host
    kubectl debug node/mynode -it --image=busybox

## Copy the failing container, examining it with ubuntu image:

    kubectl debug -it -n monitoring nginx-proxy-$HASH --image ubuntu --target nginx
    
    cat /proc/1/root/usr/share/nginx/html/index.html

Using the same image as the original container:

    kubectl debug -it k8sdebug-8449c77884-t5zbs --image k8sdebug:0.0.1-SNAPSHOT --target k8sdebug

What you can consider making, is an image which is functionally identical, but that
contains a shell for debugging. And possibly other tools: `k8sdebug-dev:0.0.1-SNAPSHOT`

# Distroless images

https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0

Dockerfile:

    FROM alpine
    USER 65532

Top level doc:
- https://iximiuz.com/en/posts/docker-debug-slim-containers/
- Adops the point of the ephemeral container attachment:
  https://iximiuz.com/en/posts/kubernetes-ephemeral-containers/

Element below will only work if running image is of the same type.
```shell
kubectl debug -it k8sdebug-8449c77884-7ww64 --image bellsoft/liberica-openjdk-debian:23 --target k8sdebug
```

Now try jcmd: It does not work.

kubectl debug -it k8sdebug-8449c77884-7ww64 --share-processes --image bellsoft/liberica-openjdk-debian:23 --target k8sdebug