# Flux system

This was created with
```shell
flux install \
     --components=source-controller,kustomize-controller,helm-controller,notification-controller \
     --components-extra=image-reflector-controller,image-automation-controller \
     --export > ./system/gotk-components.yaml
```
It can be updated the same way when the `flux` runtime has been updated.

## Future direction

At some time it might be prudent to separate the flux setup into
several git repositories. The reasons are multiple:
- You can different access rights with different git repos
- As all setup originates from the same place, there is a lag when one resource waits for another