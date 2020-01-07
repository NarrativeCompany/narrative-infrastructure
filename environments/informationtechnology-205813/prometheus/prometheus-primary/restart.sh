#!/bin/bash
# error if anything bad happens
set -e

echo "Setting the context..."
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

echo "Checking for the PVC..."
kubectl -n it get pvc | grep prometheus-data | grep Bound || { echo "prometheus-data not found or not bound. Prometheus data PVC must be created first. Exiting..." && exit 1; }

echo "Checking for the narrative-prometheus-federation secret..."
kubectl -n it get secret narrative-prometheus-federation

echo "Checking for the prometheus service account..."
kubectl -n it get serviceaccount prometheus

if kubectl -n it get pods | grep prometheus; then
  echo "Deleting pod to reload config..."
  kubectl -n it get pods | grep prometheus | awk '{ print $1 }' | xargs -I {} kubectl -n it delete pod {}
fi

echo "Sleeping 5 seconds before checking pod status..."
sleep 5
kubectl -n it get pods
