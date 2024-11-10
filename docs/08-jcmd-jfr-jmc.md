# JCMD, JFR and JMC

There are several ways of getting a Java Flight Recorder (JFR) recording. You
can give command line options, or get a JDR recording started ad hoc. The 
latter is what I will cover here.

## JCMD

First question is: Do you have JCMD available? If you do not, you want to
get it installed somehow.

### Copy tools out of image

If uncertain what is inside the image:
```
docker run --rm -it bellsoft/liberica-openjdk-debian:23-cds bash
```

Here, we just copy a JDK out of an image. An alternative would be to explode a
distribution, and use that. This feels somewhat easier, making me more confident
that the JDK is compatible.
```shell
IMAGE=bellsoft/liberica-openjdk-debian:23-cds
docker pull --platform linux/amd64 $IMAGE
docker create  --platform linux/amd64 --name tmp-del $IMAGE
docker cp tmp-del:/usr/lib/jvm/jdk-23-bellsoft-x86_64/ jdk
```

Then you copy the full jdk into your container:

```shell
export POD=$(kubectl get pods -o=jsonpath='{range .items..metadata}{.name}{"\n"}{end}'|grep k8sdebug)
kubectl cp jdk $POD:/tmp/jdk
kubectl exec -it $POD -- bash
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
jcmd 7 JFR.dump name=1 filename=/tmp/filename1.jfr
```

Copy out the image to your local machine:
```
kubectl cp k8sdebug-$HASH:/tmp/filename1.jfr filename1.jfr
```

Will probably work best if the same distro.

### Jattach

To copy a whole JDK feels both wasteful, and dirty. An alternative is to use
the jattach tool (if you trust its origins):
[https://github.com/jattach/jattach/releases](https://github.com/jattach/jattach/releases)

```shell
export POD=$(kubectl get pods -o=jsonpath='{range .items..metadata}{.name}{"\n"}{end}'|grep k8sdebug)
curl -L -o - https://github.com/jattach/jattach/releases/download/v2.2/jattach-linux-x64.tgz|tar -xzf -
kubectl cp jattach $POD:/tmp/jattach
```

What if you get:

```
level=error msg="exec failed: unable to start container process: exec: \"tar\": executable file not found in $PATH"
```
Just pipe it:
```
cat jattach| kubectl exec -i POD-$HASH -- bash -c "cat > /tmp/jattach" 
```

The commands are then analogous to jcmd from the JDK distribution.

```
kubectl exec -it k8sdebug-app-$HASH -- bash
/tmp/jattach 7 jcmd help
/tmp/jattach 7 jcmd JFR.start
```

```
/tmp/jattach 7 jcmd JFR.start
/tmp/jattach 7 jcmd JFR.stop
/tmp/jattach 7 jcmd JFR.dump name=1 filename=$PWD/filename1.jfr

jcmd 7 GC.class_histogram|more
```

## JMC

Java Mission Control (JMC) is a GUI for JFR:
[https://github.com/openjdk/jmc](https://github.com/openjdk/jmc)

Let's get a more representative dump:
- suspend flux
- use the image with the "manual" tag
- run the tests

```shell
kubectl create -k ../clusters/kind/apps/k6test/
```