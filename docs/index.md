# Plan

Link to these pages:
![Slides](images/qr-to-github.png)

- We have a Kubernetes cluster
    - ... in which there is an example java application
    - Alternatives for building docker image
    - Metrics and graphs
    - K6 soak testing

- What to do when it is not running correctly
    - Connecting to application in debug mode
    - Java Flight Recorder
    - Java Mission Control
    - Other tools and tricks of the trade
    - What have we learned?

## What to learn

- More options to debug applications in Kubernetes
- Soak testing
- Metrics help you
    - but determine baseline beforehand
- Plan ahead:
  - How are you going to debug an application?
    - Give and revoke temporary access
    - Method for container introspection
    - Document how to attach a container
