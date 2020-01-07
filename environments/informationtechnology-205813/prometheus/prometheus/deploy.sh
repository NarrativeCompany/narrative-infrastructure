#!/bin/bash
# error if anything bad happens
set -e

echo "Setting the context..."
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

echo "Checking for the PVC..."
kubectl -n monitoring get pvc | grep prometheus-data | grep Bound || { echo "prometheus-data not found or not bound. Prometheus data PVC must be created first. Exiting..." && exit 1; }

# not needed in the IT cluster
# echo "Checking for the narrative-prometheus-federation secret..."
# kubectl -n monitoring get secret narrative-prometheus-federation

echo "Creating service account and bindings..."
kubectl apply -f service-account/prometheus-role-bindings.yml
kubectl apply -f service-account/prometheus-roles.yml
kubectl apply -f service-account/prometheus-service-account.yml

echo "Checking for the prometheus service account..."
kubectl -n monitoring get serviceaccount prometheus

echo "Deploying prometheus-config..."
kubectl -n monitoring create configmap prometheus-config --from-file=config/prometheus.yml  --dry-run -o json | kubectl apply -f -
echo "Deploying prometheus-rules"
kubectl -n monitoring create configmap prometheus-rules --from-file=rules/ --dry-run -o json | kubectl apply -f -

echo "Pause 30 seconds to let configmaps update..."
sleep 30

if kubectl -n monitoring get pods | grep -v prometheus-operator | grep -v kube-prometheus | grep prometheus | grep Running; then
  echo "reloading config"
  kubectl -n monitoring get pods | grep -v prometheus-operator | grep -v kube-prometheus | grep prometheus | awk '{ print $1 }' | xargs -I {} kubectl -n monitoring exec {} -- /bin/sh -c "kill -HUP 1"
fi

echo "Deploying prometheus deployment..."
 kubectl apply -f prometheus-deployment.yml

echo "Deploying prometheus service..."
kubectl apply -f prometheus-service.yml
echo "Deploying prometheus ingress..."
kubectl apply -f it-prometheus-narrative-cloud-ingress.yml
