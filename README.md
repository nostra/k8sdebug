#  Kubernetes Java Debugging 

Original description:
    Debug java minnebruk og slikt med jcmd, jmc, jfr, og lignende. 
    Hvordan å gjøre dette for Java-app i Kubernetes. 
    Hva å gjøre om verktøy ikke er tilgjengelig i kjørende image. 
    Hvordan integrere Laptop-lokal applikasjon i kjørende k8s-cluster.

# Mkdocs

Use `mkdocs serve` to preview the docs locally.


# How to attach with debugger

TODO Show port-fw to debug port


# What to do with no restart

You have an application in production, but you cannot restart
it to attach a debugger. Maybe you are not allowed to, and
you need to give instructions to infra to do something for you. 
What do you do?

## Debug with jcmd:
```
kubectl exec -it k8sdebug-$HASH -- bash
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
kubectl cp /tmp/jattach k8sdebug-786f68c99b-jqc64:/tmp/jattach
kubectl exec -it k8sdebug-786f68c99b-jqc64 -- bash
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

# Java debug with IntelliJ

-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

# k6

https://grafana.com/docs/k6/latest/set-up/install-k6/

brew install k6
