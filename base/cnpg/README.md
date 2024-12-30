# CNPG

- https://github.com/cloudnative-pg/cloudnative-pg/tree/main
- https://github.com/cloudnative-pg/cloudnative-pg/blob/main/docs/src/installation_upgrade.md

## Install plugin for cnpg

```bash
kubectl krew install cnpg
```

## Install or update

Check version number from:
https://github.com/cloudnative-pg/cloudnative-pg/releases

```bash
VERSION="1.25"
curl -L -o cnpg.yaml "https://raw.githubusercontent.com/cloudnative-pg/cloudnative-pg/release-${VERSION}/releases/cnpg-${VERSION}.0.yaml"
```


