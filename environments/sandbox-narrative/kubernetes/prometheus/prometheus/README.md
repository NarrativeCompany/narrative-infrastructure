# prometheus

This deploys a custom Prometheus deployment. Note that this is NOT the helm installed Prometheus.

Requires cluster admin:

```
# cluster-admin setup
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: cluster-admins
subjects:
- kind: User
  name: <YOURUSER>
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: ""
```

Also requires the `cloudflare-wildcard-narrative-org` secret in the `monitoring` namespace.

# Configure TLS Secrets

Create two files, `tls.crt` and `tls.key`, found in 1Password (Operations vault, `*.narrative.org Cloudflare wildcard - 3 year`).

Create the secrets:

```
kubectl -n monitoring create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
```

Delete the files when done. (`tls.key` and `tls.crt`)

# Federation Setup

Run the apache docker image to create the htaccess secret:

```
docker run --rm -it --entrypoint=/bin/bash httpd:latest
```

Create the secret:

```
htpasswd -c auth narrative-prometheus-federation
New password:
Re-type new password:
```

Save this username/password to 1Password (Operations vault, `narrative-prometheus-federation`).

Create a file from this data named `auth` using the `htaccess auth` secret. Create the Kubernetes secret and delete the file:

```
kubectl -n monitoring create secret generic narrative-prometheus-federation --from-file=auth
```

# The Prometheus Client TLS Secret

To create the TLS secret, get the files from 1Password, `prometheus-client TLS`:

```
kubectl config use-context gke_sandbox-narrative_us-east1_it-cluster
kubectl -n monitoring create secret tls prometheus-tls-client --cert=client.pem --key=client-key.pem
kubectl -n monitoring create configmap prometheus-tls-client-ca --from-file=ca.pem
```

# GCE Configuration

The sandbox Prometheus server will use GCE discovery to find and monitor remote endpoints. The credentials can be found in 1Password, in the Operations Vault, named `prometheus.json GCE`. Create the `prometheus.json` and `ca.pem` file.

Add the policy:

```
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:prometheus@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.viewer
```

Now add the secret to Kubernetes:

```
kubectl config use-context gke_sandbox-narrative_us-east1_it-cluster
kubectl -n monitoring create secret generic prometheus-gce --from-file ./prometheus.json
kubectl -n monitoring create configmap prometheus-tls-client-ca --from-file ./ca.pem
```

**Delete the `prometheus.json` file and ca.pem files.**
