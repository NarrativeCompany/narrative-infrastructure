#!/bin/bash
# error if anything bad happens
set -e

echo "Setting the context..."
kubectl config use-context gke_sandbox-narrative_us-east1_sandbox-cluster

echo "Deploying prometheus-config..."
kubectl -n monitoring create configmap prometheus-config --from-file=config/prometheus.yml  --dry-run -o json | kubectl apply -f -
echo "Deploying prometheus-rules"
kubectl -n monitoring create configmap prometheus-rules --from-file=rules/ --dry-run -o json | kubectl apply -f -

echo "Pause 30 seconds to let configmaps update..."
sleep 30

echo "reloading config"
kubectl -n monitoring get pods | grep -v prometheus-operator | grep "^prometheus" | awk '{ print $1 }' | xargs -I {} kubectl -n monitoring exec {} -- /bin/sh -c "kill -HUP 1"
