# Overview

`production-cluster` is a Google Kubernetes Environment, running in the `production-narrative` Google project. It can be found [here](https://console.cloud.google.com/kubernetes/list?project=production-narrative&organizationId=149049775531).

# Requirements

- Google Cloud account with GKE access.
- Kubernetes `kubectl` binary.
- Helm installed via Brew. (`brew install kubernetes-helm`)

# Setup

Configure `kubectl`:

```
gcloud container clusters get-credentials production-cluster --region us-central1 --project production-narrative
```

Verify it's the default:

```
➜  kubectl config get-contexts
CURRENT   NAME                                                      CLUSTER                                                   AUTHINFO                                                  NAMESPACE
          docker-for-desktop                                        docker-for-desktop-cluster                                docker-for-desktop
          gke_informationtechnology-205813_us-east1_it-cluster      gke_informationtechnology-205813_us-east1_it-cluster      gke_informationtechnology-205813_us-east1_it-cluster
*         gke_production-narrative_us-central1_production-cluster   gke_production-narrative_us-central1_production-cluster   gke_production-narrative_us-central1_production-cluster
          gke_sandbox-narrative_us-east1_sandbox-cluster            gke_sandbox-narrative_us-east1_sandbox-cluster            gke_sandbox-narrative_us-east1_sandbox-cluster
          gke_www-narrative_us-east1_www-cluster                    gke_www-narrative_us-east1_www-cluster                    gke_www-narrative_us-east1_www-cluster
```

You can always set it just to be sure:

```
kubectl config use-context gke_production-narrative_us-central1_production-cluster
```

Configure cluster admin:

```
kubectl create clusterrolebinding cluster-admin-binding-USERNAME --clusterrole=cluster-admin --user=USERNAME@narrative.org
```

# Overview

This describes the process for deploying the Kubernetes deployments for supporting the Narrative platform and services.

# Helm

- Initialize:
-
```
kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
helm init --upgrade --service-account tiller
```

# nginx-ingress

Follow the README in `kubernetes/nginx-ingress`. Once deployed, return to this document.

# Create the Namespaces

Create the namespaces:

```
kubectl create namespace production
```

# Configure TLS Secrets

Create two files, `tls.crt` and `tls.key`, found in 1Password (Operations vault, `*.narrative.org Cloudflare wildcard - 3 year`).

Create the secrets:

```
kubectl -n default create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
kubectl -n production create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
```

Delete the files when done. (`tls.key` and `tls.crt`)

# docker-registry

Narrative Docker hub registry credentials in Kubernetes. This can be found in 1Password (Operations vault, `narrativeops Docker hub`).

```
kubectl -n default create secret docker-registry narrativecompany-docker --docker-server=https://index.docker.io/v1/ --docker-username=narrativeops --docker-password=SECRETPASSWORD --docker-email=ops@narrative.network
```

This will need to be in every namespace that needs access to private Docker images.

# oauth

Follow the README in `oauth2_proxy`.

# postfix

Follow the README in `postfix`.

# prometheus

Follow the README in `prometheus`.
