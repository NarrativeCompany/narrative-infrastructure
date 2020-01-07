#!/bin/bash
# error if anything bad happens
set -e

echo "Setting the context..."
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

echo "Checking for the PVC..."
kubectl -n it get pvc | grep prometheus-data | grep Bound || { echo "prometheus-data not found or not bound. Prometheus data PVC must be created first. Exiting..." && exit 1; }

echo "Checking for the narrative-prometheus-federation secret..."
kubectl -n it get secret narrative-prometheus-federation

echo "Deploying prometheus-config..."
kubectl -n it create configmap prometheus-config --from-file=config/prometheus.yml  --dry-run -o json | kubectl apply -f -
echo "Deploying prometheus-rules"
kubectl -n it create configmap prometheus-rules --from-file=rules/ --dry-run -o json | kubectl apply -f -

echo "Pause 30 seconds to let configmaps update..."
sleep 30

if kubectl -n it get pods | grep prometheus; then
  echo "reloading config"
  kubectl -n it get pods | grep prometheus | awk '{ print $1 }' | xargs -I {} kubectl -n it exec {} -- /bin/sh -c "kill -HUP 1"
fi

echo "Deploying prometheus deployment..."
 kubectl apply -f prometheus-deployment.yml

echo "Deploying prometheus service..."
kubectl apply -f prometheus-service.yml
echo "Deploying prometheus ingress..."
kubectl apply -f prometheus-narrative-cloud-ingress.yml
