# Overview

`sandbox-cluster` is a Google Kubernetes Environment, running in the `sandbox-narrative` Google project. It can be found [here](https://console.cloud.google.com/kubernetes/list?project=sandbox-narrative&organizationId=149049775531).

# Requirements

- Google Cloud account with GKE access.
- Kubernetes `kubectl` binary.
- Helm installed via Brew. (`brew install kubernetes-helm`)

# Setup

Configure `kubectl`:

```
gcloud container clusters get-credentials sandbox-cluster --region us-east1 --project sandbox-narrative
```

Verify it's the default:

```
➜  kubectl config get-contexts
CURRENT   NAME                                                   CLUSTER                                                AUTHINFO                                               NAMESPACE
          gke_informationtechnology-205813_us-east1_it-cluster   gke_informationtechnology-205813_us-east1_it-cluster   gke_informationtechnology-205813_us-east1_it-cluster   
*         gke_sandbox-narrative_us-east1_sandbox-cluster         gke_sandbox-narrative_us-east1_sandbox-cluster         gke_sandbox-narrative_us-east1_sandbox-cluster         
          gke_www-narrative_us-east1_www-cluster                 gke_www-narrative_us-east1_www-cluster                 gke_www-narrative_us-east1_www-cluster        
```

Configure cluster admin:

```
kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=jeff.jordan@narrative.org
```

# Overview

This describes the process for deploying the Kubernetes deployments for supporting the Narrative websites.

# Helm

- Initialize:
-
```
kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
helm init --upgrade --service-account tiller
```

# nginx-ingress

Follow the README in `kubernetes/nginx-ingress`.

# Create the Namespaces

Create two namespaces:

```
kubectl create namespace dev
kubectl create namespace staging
```

# Configure TLS Secrets

Create two files, `tls.crt` and `tls.key`, found in 1Password (Operations vault, `*.narrative.org Cloudflare wildcard - 3 year`).

Create the secrets:

```
kubectl -n default create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
kubectl -n dev create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
kubectl -n staging create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
```

Delete the files when done. (`tls.key` and `tls.crt`)

# docker-registry

Narrative Docker hub registry credentials in Kubernetes. This can be found in 1Password (Operations vault, `narrativeops Docker hub`).

```
kubectl create secret docker-registry narrativecompany-docker --docker-server=https://index.docker.io/v1/ --docker-username=narrativeops --docker-password=SECRETPASSWORD --docker-email=ops@narratie.network
```

This will need to be in every namespace that needs access to private Docker images.

# oauth

Follow the README in `kubernetes/oauth2_proxy`.

# prometheus

Follow the README in `kubernetes/prometheus`.
