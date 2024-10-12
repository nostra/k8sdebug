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
kind load docker-image jk8sdebug:0.0.1-SNAPSHOT --name atlas
kubectl create -k .
```

# Debug with jcmd:
```
kubectl exec -it jk8sdebug-$HASH -- bash
jcmd
jcmd 1
```

