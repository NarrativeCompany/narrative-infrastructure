# Instructions on Setting up Google Cloud

Google Cloud configuration and deployment information.

# Requirements 

These instructions assume that you:

- have a Google Cloud account
- have the Google Cloud SDK installed and configured.
- Terraform version 0.10.X or 0.11.X

# Google Kubernetes Engine

The `it-cluster` was created via the GUI. The CLI commands to create it:

```
gcloud beta container --project "informationtechnology-205813" clusters create "it-cluster" --region "us-east1" --username "admin" --cluster-version "1.9.7-gke.1" --machine-type "n1-standard-4" --image-type "COS" --disk-type "pd-standard" --disk-size "100" --scopes "https://www.googleapis.com/auth/compute","https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/monitoring","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append" --num-nodes "1" --enable-cloud-logging --enable-cloud-monitoring --network "default" --subnetwork "default" --enable-autoscaling --min-nodes "1" --max-nodes "3" --enable-network-policy --addons HorizontalPodAutoscaling,HttpLoadBalancing,KubernetesDashboard --enable-autoupgrade --enable-autorepair
```

To connect to it:

```
export CLOUDSDK_CONTAINER_USE_V1_API_CLIENT=false && export CLOUDSDK_CONTAINER_USE_V1_API=false && gcloud beta container clusters get-credentials it-cluster --region us-east1 --project informationtechnology-205813
```

