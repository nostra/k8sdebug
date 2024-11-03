# Attach Kubernetes debugger


kubectl debug --fork will copy the entire Pod spec

https://dev.to/chainguard/debugging-distroless-images-with-kubectl-and-cdebug-1dm0

Dockerfile:

    FROM alpine
    USER 65532

Top level doc:
- https://iximiuz.com/en/posts/docker-debug-slim-containers/
- Adops the point of the ephemeral container attachment:
  https://iximiuz.com/en/posts/kubernetes-ephemeral-containers/