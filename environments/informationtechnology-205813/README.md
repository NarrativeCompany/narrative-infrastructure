# Overview

The `informationtechnology-205813` environment is a Google Kubernetes Environment, running in the `informationtechnology-205813` Google project. It can be found [here](https://console.cloud.google.com/kubernetes/list?project=informationtechnology-205813&organizationId=149049775531).

# Requirements

- Google Cloud account with GKE access.
- Kubernetes `kubectl` binary.
- Helm installed via Brew.

# Setup

Configure `kubectl`:

```
gcloud container clusters get-credentials www-cluster --region us-east1 --project informationtechnology-205813
```

Verify it's the default:

```
➜  kubectl config get-contexts
CURRENT   NAME                                                   CLUSTER                                                AUTHINFO                                               NAMESPACE
          dev.narrative.cloud                                    dev.narrative.cloud                                    dev.narrative.cloud
          docker-for-desktop                                     docker-for-desktop-cluster                             docker-for-desktop
*         gke_informationtechnology-205813_us-east1_it-cluster   gke_informationtechnology-205813_us-east1_it-cluster   gke_informationtechnology-205813_us-east1_it-cluster
          gke_www-narrative_us-east1_www-cluster                 gke_www-narrative_us-east1_www-cluster                 gke_www-narrative_us-east1_www-cluster
```

Configure cluster admin:

```
kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=bill.cawthra@narrative.network
```

# Overview

This describes the process for deploying the Kubernetes deployments in support of the `informationtechnology` project.

# Helm

- Initialize:
-
```
kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
helm init --upgrade --service-account tiller
```

# prometheus deployment

```
helm repo add coreos https://s3-eu-west-1.amazonaws.com/coreos-charts/stable/
helm install coreos/prometheus-operator --name prometheus-operator --namespace monitoring
# helm install coreos/kube-prometheus --name kube-prometheus --namespace monitoring --set deployGrafana=false --set deployAlertManager=false --set global.rbacEnable=true --set exporter-kubelets.https=false
helm install -f prometheus-k8s/values.yml
```

# nginx-ingress

To deploy the nginx-ingress-controller (only deploys a public ingress controller at this time).

```
helm install -f nginx-ingress/values.yml --name nginx-ingress stable/nginx-ingress
# to upgrade if modified
helm upgrade -f nginx-ingress/values.yml
```

The current endpoint IP address is `35.237.70.181`.

# Create the Namespaces

Create the namespaces:

```
kubectl create namespace it
kubectl create namespace jenkins
kubectl create namespace upsource
```

# Cloudflare Origin CA

Get the `tls.crt` and `tls.key` from 1Password. Create the Cloudflare secret:

```
kubectl -n it create secret tls cloudflare-wildcard-narrative-cloud --key tls.key --cert tls.crt
kubectl -n jenkins create secret tls cloudflare-wildcard-narrative-cloud --key tls.key --cert tls.crt
kubectl -n upsource create secret tls cloudflare-wildcard-narrative-cloud --key tls.key --cert tls.crt
kubectl -n default create secret tls cloudflare-wildcard-narrative-cloud --key tls.key --cert tls.crt
```
