#!/bin/bash
alert='[
  {
    "status": "firing",
    "labels": {
      "alertname": "TEST-it-prometheus",
      "condition": "true",
      "endpoint": "kube-state-metrics",
      "instance": "10.56.2.7:8080",
      "job": "kube-state",
      "namespace": "staging",
      "pod": "narrative-core-TESTING-FOO",
      "service": "kube-prometheus-exporter-kube-state",
      "severity": "warning"
    },
    "annotations": {
      "message": "TESTING THINGS",
      "runbook_url": "https://www.google.com"
    },
    "generatorURL": "https://prometheus.narrative.cloud"
  }
]'

kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

POD=$(kubectl -n monitoring get pods | grep alertmanager | awk '{ print $1 }')
kubectl -n monitoring port-forward ${POD} 9093:9093 &
FORWARDER=$!
echo "$FORWARDER"

echo "Pausing 5 seconds..."
sleep 5

curl -XPOST -d"$alert" 'http://127.0.0.1:9093/api/v1/alerts'

kill ${FORWARDER}
