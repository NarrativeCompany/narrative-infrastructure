This deploys Prometheus, Alertmanager, and the Blackbox-Exporter  to Google Kubernetes Engine, to enable monitoring within the Kubernetes cluster in the `it-narrative` GKE cluster in Google Cloud.

This deploys two Prometheus stacks:

- The Prometheus primary system (<https://prometheus.narrative.cloud>), which monitors all disparate environments.
- The Prometheus it-cluster system, which monitors the Kubernetes it cluster.

Environments deployed:

- <https://prometheus.narrative.cloud>
- <https://alertmanager.narrative.cloud>
- <https://grafana.narrative.cloud>
- <https://it-prometheus.narrative.cloud>
- <https://it-alertmanager.narrative.cloud>

# Deploy the IT Prometheus and Alertmanager stack

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

Deploy Alertmanager. You'll need the PagerDuty and Slack webhook environment variables set (found in 1Password `alertmanager pagerduty integration key and slack integration and bearer token`):

```
export PAGERDUTY_API_KEY=<1PASSWORD>
export SLACK_WEB_HOOK=<1PASSWORD>
export BEARER_TOKEN=<1PASSWORD>
cd alertmanager
./deploy.sh
```

Configure Cloudflare DNS to point to the `it-narrative` GKE cluster ingress (<https://console.cloud.google.com/kubernetes/list?project=it-narrative&organizationId=149049775531>). Set both `it-prometheus.narrative.cloud` and `it-alertmanager.narrative.cloud` A records.

Configure the oauth settings (<https://console.cloud.google.com/apis/credentials/oauthclient/1026630561657-4kkm1aoe71ldcvp4rkd886ghjgqemv8a.apps.googleusercontent.com?project=operations-204322&organizationId=149049775531>). This requires two redirects for each site:

```
https://it-prometheus.narrative.cloud
https://it-prometheus.narrative.cloud/oauth2/callback
https://it-alertmanager.narrative.cloud
https://it-alertmanager.narrative.cloud/oauth2/callback
```

Verify that <https://it-prometheus.narrative.cloud> and <https://it-alertmanager.narrative.cloud> are accessible.

To deploy the IT blackbox-exporter:

```
cd blackbox-exporter
./deploy.sh
```

# Deploy the primary Prometheus, Alertmanager, and Grafana Stack

Deploy Prometheus:

```
cd prometheus-primary
./deploy.sh
```

Deploy Alertmanager. You'll need the PagerDuty and Slack webhook environment variables set (found in 1Password):

```
export PAGERDUTY_API_KEY=<1PASSWORD>
export SLACK_WEB_HOOK=<1PASSWORD>
cd alertmanager-primary
./deploy.sh
```

Deploy Grafana. The Grafana secret, `grafana-credentials`, is required.

```
cd grafana-primary
./deploy.sh
```



