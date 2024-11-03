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

Using the same type, to easy loading:
```shell
kubectl debug -it k8sdebug-$HASH --image bellsoft/liberica-openjdk-debian:23 --target k8sdebug
```

Did not work:
kubectl debug -it k8sdebug-$HASH --share-processes --image bellsoft/liberica-openjdk-debian:23 --copy-to=debug 

# Jattach

https://github.com/jattach/jattach/releases

```shell
curl -L -o - https://github.com/jattach/jattach/releases/download/v2.2/jattach-linux-x64.tgz|tar -xzf -
kubectl cp jattach atlas-app-$HASH:/tmp/jattach
```

```
kubectl exec -it atlas-app-$HASH -- bash
/tmp/jattach 1 jcmd help
/tmp/jattach 1 jcmd JFR.start
```

```
/tmp/jattach 7 jcmd JFR.start
/tmp/jattach 7 jcmd JFR.stop
/tmp/jattach 7 jcmd JFR.dump name=1 filename=$PWD/filename1.jfr
```

## Copy tools out of image

```bash
IMAGE=bellsoft/liberica-openjdk-debian:23-cds
docker pull --platform linux/amd64 $IMAGE
docker create  --platform linux/amd64 --name tmp-del $IMAGE
docker cp tmp-del:/usr/lib/jvm/jdk-23-bellsoft-x86_64/ jdk
```

Then you copy into your container
```
kubectl cp jdk k8sdebug-$HASH:/tmp/jdk
kubectl exec -it k8sdebug-786f68c99b-jqc64 -- bash
```
Inside the container set the path:
```
java -version
export JAVA_HOME=/tmp/jdk
export PATH=/tmp/jdk/bin:$PATH
java -version
jcmd
jcmd 7 JFR.start
jcmd 7 JFR.stop
jcmd 7 JFR.dump name=1 filename=$PWD/filename1.jfr
```

If uncertain what is inside the image:
```
docker run --rm -it bellsoft/liberica-openjdk-debian:23-cds bash
```

Will probably work best if the same distro.
