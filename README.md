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

Pro-tip: Remove probes in order to avoid Kubernetes restart of pod when sleeping on breakpoint.

-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

kubectl port-forward -n <namespace> <pod-name> 5005:5005

# k6

https://grafana.com/docs/k6/latest/set-up/install-k6/

brew install k6
