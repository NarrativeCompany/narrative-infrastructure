# prometheus

This deploys Prometheus, Alertmanager,  to Google Kubernetes Engine, to enable monitoring within the Kubernetes cluster in the `staging-narrative` GKE cluster in Google Cloud.

# Deploy Prometheus and Alertmanager

Add the repository:

```
helm repo add coreos https://s3-eu-west-1.amazonaws.com/coreos-charts/stable/
```

Deploy the prometheus-operator:

```
# installs the operator
helm install coreos/prometheus-operator --name prometheus-operator --namespace monitoring
# installs the monitoring components
helm install coreos/kube-prometheus --name kube-prometheus --namespace monitoring --values values.yml
```

To deploy the blackbox-exporter:

```
cd blackbox-exporter
./deploy.sh
```

Deploy Prometheus:

```
cd prometheus
kubectl apply -f pvc.yml
./deploy.sh
```

Deploy Alertmanager. You'll need the PagerDuty and Slack webhook environment variables set found in 1Password (Operations vault, `alertmanager pagerduty integration key` and `prometheus slack webhook`):

```
export PAGERDUTY_API_KEY=<1PASSWORD>
export SLACK_WEB_HOOK=<1PASSWORD>
cd alertmanager
./deploy.sh
```

Configure Cloudflare DNS to point to the `staging-narrative` GKE cluster ingress (<https://console.cloud.google.com/kubernetes/list?project=staging-narrative&organizationId=149049775531>). Set both `staging-prometheus.narrative.cloud` and `staging-alertmanager.narrative.cloud` A records.

Configure the oauth settings (<https://console.cloud.google.com/apis/credentials/oauthclient/1026630561657-4kkm1aoe71ldcvp4rkd886ghjgqemv8a.apps.googleusercontent.com?project=operations-204322&organizationId=149049775531>). This requires two redirects for each site:

```
https://staging-prometheus.narrative.cloud
https://staging-prometheus.narrative.cloud/oauth2/callback
https://staging-alertmanager.narrative.cloud
https://staging-alertmanager.narrative.cloud/oauth2/callback
```

Verify that <https://staging-prometheus.narrative.cloud> and <https://staging-alertmanager.narrative.cloud> are accessible.

