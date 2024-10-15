#  Kubernetes Java Debugging 

Debug java minnebruk og slikt med jcmd, jmc, jfr, og lignende. 
Hvordan å gjøre dette for Java-app i Kubernetes. 
Hva å gjøre om verktøy ikke er tilgjengelig i kjørende image. 
Hvordan integrere Laptop-lokal applikasjon i kjørende k8s-cluster.

## Problemstilling

Lag spring-boot-app med micrometer, og få lastet metrikker inn i den, kanskje med k6

Se minnebruk

## Build / deploy

Build:
```
cd spring-app
./mvnw package spring-boot:build-image
```

# Alternative docker build

https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html

```
cd spring-app

./mvnw package 
docker build -t jk8sdebug:manual .
```

# Deploy in demo namespace:
```
cd k8s
kind load docker-image jk8sdebug:manual --name atlas
kind load docker-image jk8sdebug:0.0.1-SNAPSHOT --name atlas
kubectl create -k .
```

# How to attach with debugger

TODO Show port-fw to debug port

# Insert your laptop into running cluster

TODO Jkube
Eclipse JKube can hijack a service in the cluster on your
behalf. Doc:
https://eclipse.dev/jkube/docs/kubernetes-maven-plugin/#jkube:remote-dev

You need to suspend flux, so the configuration does not get reset. You
also need to configure Kubernetes so you are in the correct namespace.

Steps:
- Check output from currently running cluster: http://atlas-app.local.gd:31090/
- Ensure you have a connection to k8s, and that you have the apps namespace
  active. You should see the atlas pod:
```
kubectl config set-context --current --namespace=apps
kubectl get pods
```
- Suspend Flux, so it does not reset config:
```
flux suspend kustomization atlas
```
- Start atlas locally, change output
- pom.xml has configuration for hijack. Do:
```
./mvnw k8s:remote-dev
```
- Check output again: http://atlas-app.local.gd:31090/

With the steps above, you should observe output from your locally
running application.

When done, stop maven job with CTRL+C, and remember to resume Flux:
```
flux resume kustomization atlas
```

# What to do with no restart

You have an application in production, but you cannot restart
it to attach a debugger. Maybe you are not allowed to, and
you need to give instructions to infra to do something for you. 
What do you do?

## Debug with jcmd:
```
kubectl exec -it jk8sdebug-$HASH -- bash
jcmd
jcmd 1
```

# When you do not have jcmd

## Alternative: Get tool from same distro

Will probably work best if the same distro:

```bash
IMAGE=bellsoft/liberica-openjdk-debian:23-cds
docker pull --platform linux/amd64 $IMAGE
docker create  --platform linux/amd64 --name tmp-del $IMAGE
docker cp tmp-del:/usr/lib/jvm/jdk-23-bellsoft-x86_64/ jdk
```

Then you copy into your container
```
kubectl cp jdk jk8sdebug-$HASH:/tmp/jdk
kubectl exec -it jk8sdebug-786f68c99b-jqc64 -- bash
```
Inside the container set the path:
```
java -version
export JAVA_HOME=/tmp/jdk
export PATH=/tmp/jdk/bin:$PATH
java -version
jcmd
```

If uncertain what is inside the image:
```
docker run --rm -it bellsoft/liberica-openjdk-debian:23-cds bash
```

## Alternative: Jattach

- https://github.com/jattach/jattach/
- https://github.com/jattach/jattach/releases

```bash
VERSION=v2.2
curl -L -o /tmp/jattach https://github.com/jattach/jattach/releases/download/$VERSION/jattach
```

```
kubectl cp /tmp/jattach jk8sdebug-786f68c99b-jqc64:/tmp/jattach
kubectl exec -it jk8sdebug-786f68c99b-jqc64 -- bash
```

```
chmod a+x /tmp/jattach 
/tmp/jattach
/tmp/jattach 1 jcmd help VM.version
/tmp/jattach 1 jcmd VM.version
```

## What if you cannot write to tmp

Create an RW empty volume. This requires container restart.


# Java Flight Recorder

export PATH=$PATH:/layers/paketo-buildpacks_bellsoft-liberica/jre/bin/
jcmd 725 JFR.start
jcmd 725 JFR.stop
jcmd 725 JFR.dump name=1 filename=$PWD/filename1.jfr

## Java Mission Control

