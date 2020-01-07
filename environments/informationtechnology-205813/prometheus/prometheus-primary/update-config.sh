#!/bin/bash
# error if anything bad happens
set -e

echo "Setting the context..."
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

echo "Deploying prometheus-config..."
kubectl -n it create configmap prometheus-config --from-file=config/prometheus.yml  --dry-run -o json | kubectl apply -f -
echo "Deploying prometheus-rules"
kubectl -n it create configmap prometheus-rules --from-file=rules/ --dry-run -o json | kubectl apply -f -

echo "Pause 60 seconds to let configmaps update..."
sleep 60

echo "reloading config"
kubectl -n it get pods | grep prometheus | awk '{ print $1 }' | xargs -I {} kubectl -n it exec {} -- /bin/sh -c "kill -HUP 1"
