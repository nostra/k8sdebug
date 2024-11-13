# Java example spring boot application

A simple spring-boot application created with Spring Initializr.

- Micrometer metrics are enabled and exposed with Prometheus (##2)
    - We have a custom metric

## Build / deploy

Build:
```shell
cd ../spring-app
./mvnw package spring-boot:build-image
```

This builds and sets up the image (##3) with JiB. The setup
takes a considerable amount of time... You get an image primed with
a rather obtuse setup with values you might want to adjust.
[https://github.com/GoogleContainerTools/jib?tab=readme-ov-file#jib](https://github.com/GoogleContainerTools/jib?tab=readme-ov-file#jib)

## Alternative jib build

Just build the image yourself. Instructions:
[https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html
](https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html)

```shell
cd ../spring-app

./mvnw package 
docker build -t k8sdebug:manual .
```


## Deploy image to kind

```shell
kind load docker-image k8sdebug:0.0.1-SNAPSHOT --name k8sdebug
```
```shell
kind load docker-image k8sdebug:manual --name k8sdebug
```
