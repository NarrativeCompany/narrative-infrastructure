#!/bin/bash
# error if anything bad happens
set -e

echo "Setting the context..."
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

echo "Deploying prometheus-config..."
kubectl -n monitoring create configmap prometheus-config --from-file=config/prometheus.yml  --dry-run -o json | kubectl apply -f -
echo "Deploying prometheus-rules"
kubectl -n monitoring create configmap prometheus-rules --from-file=rules/ --dry-run -o json | kubectl apply -f -

echo "Pause 10 seconds to let configmaps update..."
sleep 10

echo "reloading config"
kubectl -n monitoring get pods | grep -v prometheus-operator | grep "^prometheus" | awk '{ print $1 }' | xargs -I {} kubectl -n monitoring exec {} -- /bin/sh -c "kill -HUP 1"

echo "Pausing 5 seconds then reloading again because of the odd interaction with the configmap volume mount..."
sleep 5

echo "reloading config"
kubectl -n monitoring get pods | grep -v prometheus-operator | grep "^prometheus" | awk '{ print $1 }' | xargs -I {} kubectl -n monitoring exec {} -- /bin/sh -c "kill -HUP 1"
