# prometheus

This deploys a custom Prometheus deployment. Note that this is NOT the helm installed Prometheus.

Set the context:

```
kubectl config use-context gke_staging-narrative_us-east1_staging-cluster
```


# Configure TLS Secrets

Create two files, `tls.crt` and `tls.key`, found in 1Password (Operations vault, `*.narrative.org Cloudflare wildcard - 3 year`).

Create the secrets:

```
kubectl -n monitoring create secret tls cloudflare-wildcard-narrative-org --key tls.key --cert tls.crt
```

**Delete the `tls.key` and `tls.crt` files.**

# Federation Setup

Copy the federation secret from 1Password (Operations vault, `narrative-prometheus-federation`).

Create a file from this data named `auth` using the `htaccess auth` secret. Create the Kubernetes secret and delete the file:

```
kubectl -n monitoring create secret generic narrative-prometheus-federation --from-file=auth
```

# The Prometheus Client TLS Secret

To create the TLS secret, get the `ca.pem`, the `client.pem`, and the `client-key.pem` files from 1Password, `prometheus-client TLS`:

```
kubectl config use-context gke_staging-narrative_us-east1_staging-cluster
kubectl -n monitoring create secret tls prometheus-tls-client --cert=client.pem --key=client-key.pem
kubectl -n monitoring create configmap prometheus-tls-client-ca --from-file=ca.pem
```

**Delete the `ca.pem`, `client.pem`, and `client-key.pem` files.**

# GCE Configuration

The staging Prometheus server will use GCE discovery to find and monitor remote endpoints. The credentials can be found in 1Password, in the Operations Vault, named `prometheus.json GCE`. Create the `prometheus.json` file.

Add the policy:

```
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:prometheus@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.viewer
```

Now add the secret to Kubernetes:

```
kubectl config use-context gke_staging-narrative_us-east1_staging-cluster
kubectl -n monitoring create secret generic prometheus-gce --from-file ./prometheus.json
```

**Delete the `prometheus.json` file.**
