# Instructions on Setting up Google Cloud

Google Cloud configuration and deployment information.

# Requirements 

These instructions assume that you:

- have a Google Cloud account
- have the Google Cloud SDK installed and configured.
- Terraform version 0.10.X or 0.11.X

# Google Kubernetes Engine

The `sandbox-cluster` was created via the GUI.

Name: sandbox-cluster  
Location type: Regional  
Zone: us-east1  
Master version: 1.10.6-gke.1  
Number of nodes (per zone): 1  
Machine Type: 2 vCPUs  

In `Advanced edit`:  
Enable autoscaling  
Minimum number of nodes (per zone): 1  
Maximum number of nodes (per zone): 3  
Enable auto-upgrade  

In `Advanced options`:  
Maintenance window: 9:00 AM Eastern (Hours shown in your local time zone)  
Enable Kubernetes Dashboard  

The CLI commands to create it:

```
gcloud beta container --project "sandbox-narrative" clusters create "sandbox-cluster" --region "us-east1" --username "admin" --cluster-version "1.10.6-gke.1" --machine-type "n1-standard-2" --image-type "COS" --disk-type "pd-standard" --disk-size "100" --scopes "https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write","https://www.googleapis.com/auth/monitoring","https://www.googleapis.com/auth/servicecontrol","https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append" --num-nodes "1" --enable-cloud-logging --enable-cloud-monitoring --network "projects/sandbox-narrative/global/networks/default" --subnetwork "projects/sandbox-narrative/regions/us-east1/subnetworks/default" --enable-autoscaling --min-nodes "1" --max-nodes "3" --addons HorizontalPodAutoscaling,HttpLoadBalancing,KubernetesDashboard --enable-autoupgrade --enable-autorepair --maintenance-window "13:00"
```

To connect to it:

```
gcloud beta container clusters get-credentials sandbox-cluster --region us-east1 --project sandbox-narrative
```
