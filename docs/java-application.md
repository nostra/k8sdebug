# Java example spring boot application

A simple spring-boot application created with Spring Initializr.

Metrics are enabled with micrometer. We are going to make a custom metric.

TODO Examine memory usage

## Build / deploy

Build:
```shell
cd spring-app
./mvnw package spring-boot:build-image
```

# Alternative docker build

https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html

```
cd spring-app

./mvnw package 
docker build -t k8sdebug:manual .
```

# Alternative II docker build: jib

See, and adapt:
https://github.com/operator-framework/java-operator-sdk/blob/main/sample-operators/mysql-schema/pom.xml#L95

Check: https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0

# Deploy in demo namespace:
```
kind load docker-image k8sdebug:manual --name k8sdebug
kind load docker-image k8sdebug:0.0.1-SNAPSHOT --name k8sdebug
```
