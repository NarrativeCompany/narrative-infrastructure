# docker-registry

Narrative Docker hub registry credentials in Kubernetes. This can be found in 1Password (Operations vault, narrativeops Docker hub).

```
kubectl create secret docker-registry narrativecompany-docker --docker-server=https://index.docker.io/v1/ --docker-username=narrativeops --docker-password=SECRETPASSWORD --docker-email=ops@narratie.network
```

This will need to be in every namespace that needs access to private Docker images.
