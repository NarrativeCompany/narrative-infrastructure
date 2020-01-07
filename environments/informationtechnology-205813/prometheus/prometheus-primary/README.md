# prometheus

This deploys a custom Prometheus deployment. Note that this is NOT the helm installed Prometheus. This is the MASTER prometheus server, who will scrape all other prometheus deployments.

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

# Create the Prometheus Password File

Prometheus requrires a password file for HTTPS basic auth. Create a file `password_file`:

```
<password from 1password>
```

Create the secret:

```
kubectl -n it create secret generic narrative-prometheus-federation --from-file=password_file
```

Delete the file `password_file`.

The username should be set in the Prometheus configuration.

# The Prometheus Client TLS Secret

To create the TLS secret, get the files from 1Password, `prometheus-client TLS`:

```
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster
kubectl -n it create secret tls prometheus-tls-client --cert=client.pem --key=client-key.pem
kubectl -n it create configmap prometheus-tls-client-ca --from-file=ca.pem
```

# GCE Configuration

The primary Prometheus server will use GCE discovery to find and monitor remote endpoints. The credentials can be found in 1Password, in the Operations Vault, named `prometheus.json GCE`.

Set the project to `informationtechnology`. My config looks like the following:

```
gcloud config configurations activate informationtechnology

```

Create the Prometheus user:

```
gcloud iam service-accounts create prometheus --display-name "Prometheus service account."
gcloud iam service-accounts keys create ~/.config/gcloud/prometheus.json --iam-account prometheus@informationtechnology-205813.iam.gserviceaccount.com

```

Add the policy:

```
gcloud projects add-iam-policy-binding neo-narrative --member serviceAccount:prometheus@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.viewer
```

Now add the secret to Kubernetes:

```
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster
kubectl -n it create secret generic prometheus-gce --from-file ~/.config/gcloud/prometheus.json
```
