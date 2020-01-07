#!/bin/bash
# error if anything bad happens
set -e
if [ -z "$PAGERDUTY_API_KEY" ]; then
  echo "Environment variable PAGERDUTY_API_KEY not set. Exiting."
  exit 1
fi

if [ -z "$SLACK_WEB_HOOK" ]; then
  echo "Environment variable SLACK_WEB_HOOK not set. Exiting."
  exit 1
fi

echo "Setting the context..."
kubectl config use-context gke_sandbox-narrative_us-east1_sandbox-cluster

echo "Deploying alertmanager-config..."
envsubst <config/config_template.yml > config/config.yml
kubectl -n monitoring create configmap alertmanager-config --from-file=config/config.yml  --dry-run -o json | kubectl apply -f -

echo "Deleting rendered config file..."
rm config/config.yml
t
echo "Deploying alertmanager-template..."
kubectl -n monitoring create configmap alertmanager-template --from-file=template/narrative.tmpl --dry-run -o json | kubectl apply -f -

echo "Pause 5 seconds to let configmaps update..."
sleep 5

if kubectl -n monitoring get pods | grep alertmanager | grep Running; then
  echo "reloading config"
  kubectl -n monitoring get pods | grep alertmanager | awk '{ print $1 }' | xargs -I {} kubectl -n monitoring exec {} -- /bin/sh -c "kill -HUP 1"
fi

echo "Pause 5 seconds AGAIN..."
sleep 5

echo "UPDATE AGAIN!"

if kubectl -n monitoring get pods | grep alertmanager | grep Running; then
  echo "reloading config"
  kubectl -n monitoring get pods | grep alertmanager | awk '{ print $1 }' | xargs -I {} kubectl -n monitoring exec {} -- /bin/sh -c "kill -HUP 1"
fi

# echo "Deploying alertmanager deployment..."
# kubectl apply -f alertmanager-deployment.yml
# echo "Deploying alertmanager service..."
# kubectl apply -f alertmanager-service.yml
# echo "Deploying alertmanager ingress..."
# kubectl apply -f sandbox-alertmanager-narrative-cloud-ingress.yml
