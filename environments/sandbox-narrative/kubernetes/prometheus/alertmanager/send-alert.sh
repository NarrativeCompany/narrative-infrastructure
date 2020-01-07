#!/bin/bash
alert='[
  {
    "status": "firing",
    "labels": {
      "alertname": "TEST-KubePodNotReady",
      "condition": "true",
      "endpoint": "kube-state-metrics",
      "instance": "10.56.2.7:8080",
      "job": "kube-state",
      "namespace": "dev",
      "pod": "narrative-core-TESTING-FOO",
      "service": "kube-prometheus-exporter-kube-state",
      "severity": "warning"
    },
    "annotations": {
      "message": "TESTING - neo-database-01 failed systemd service: neo-python.service",
      "runbook_url": "https://github.com/NarrativeCompany/narrative-infrastructure/wiki/Alertmanager-Runbooks#systemdfailed"
    },
    "generatorURL": "https://prometheus.narrative.cloud/graph?g0.expr=node_systemd_unit_state%7Bstate%3D%22failed%22%7D+%3D%3D+1&g0.tab=1"
  }
]'

kubectl config use-context gke_sandbox-narrative_us-east1_sandbox-cluster

POD=$(kubectl -n monitoring get pods | grep alertmanager | awk '{ print $1 }')
kubectl -n monitoring port-forward ${POD} 9093:9093 &
FORWARDER=$!
echo "$FORWARDER"

echo "Pausing 5 seconds..."
sleep 5

curl -XPOST -d"$alert" 'http://127.0.0.1:9093/api/v1/alerts'

kill ${FORWARDER}
