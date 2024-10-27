# Connect a debugger

Attaching a debugger to a running application. Prerequisite is
that the application is configured with the proper jvm arguments:

    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

In addition, you need to add "Remote vm debugging" to the IntelliJ debug
configuration.

