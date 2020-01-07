# prometheus

This deploys Prometheus, Alertmanager,  to Google Kubernetes Engine, to enable monitoring within the Kubernetes cluster in the `sandbox-narrative` GKE cluster in Google Cloud.

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

Deploy Prometheus:

```
cd prometheus
./deploy.sh
```

Deploy Alertmanager. You'll need the PagerDuty and Slack webhook environment variables set found in 1Password (Operations vault, `alertmanager pagerduty integration key` and `prometheus slack webhook`):

```
export PAGERDUTY_API_KEY=<1PASSWORD>
export SLACK_WEB_HOOK=<1PASSWORD>
cd alertmanager
./deploy.sh
```

Configure Cloudflare DNS to point to the `sandbox-narrative` GKE cluster ingress (<https://console.cloud.google.com/kubernetes/list?project=sandbox-narrative&organizationId=149049775531>). Set both `sandbox-prometheus.narrative.cloud` and `sandbox-alertmanager.narrative.cloud` A records.

Configure the oauth settings (<https://console.cloud.google.com/apis/credentials/oauthclient/1026630561657-4kkm1aoe71ldcvp4rkd886ghjgqemv8a.apps.googleusercontent.com?project=operations-204322&organizationId=149049775531>). This requires two redirects for each site:

```
https://sandbox-prometheus.narrative.cloud
https://sandbox-prometheus.narrative.cloud/oauth2/callback
https://sandbox-alertmanager.narrative.cloud
https://sandbox-alertmanager.narrative.cloud/oauth2/callback
```

Verify that <https://sandbox-prometheus.narrative.cloud> and <https://sandbox-alertmanager.narrative.cloud> are accessible.

To deploy the blackbox-exporter:

```
cd blackbox-exporter
./deploy.sh
```
