#!/bin/bash
alert='[
  {
    "status": "firing",
    "labels": {
     "alertname": "TEST",
      "state": "failed",
      "severity": "webhook",
      "name": "TEST1.service",
      "job": "test-exporter",
      "instance": "1.1.1.1:9143",
      "gce_public_ip": "11.11.11.11",
      "gce_project": "neo-narrative",
      "gce_private_ip": "1.1.1.1",
      "gce_instance_name": "TEST-01",
      "cluster": "narrative-sandbox-cluster"
    },
    "annotations": {
      "identifier": "TEST-01",
      "message": "TESTING message here.",
      "runbook_url": "https://github.com/NarrativeCompany/narrative-infrastructure/wiki/Alertmanager-Runbooks#systemdfailed"
    },
    "generatorURL": "https://prometheus.narrative.cloud/graph?g0.expr=node_systemd_unit_state%7Bstate%3D%22failed%22%7D+%3D%3D+1&g0.tab=1"
  }
]'

alert_webhook_2='[
  {
    "status": "firing",
    "labels": {
     "alertname": "TEST",
      "state": "failed",
      "severity": "webhook",
      "name": "TEST1.service",
      "job": "test-exporter",
      "instance": "2.2.2.2:9143",
      "gce_public_ip": "22.22.22.22",
      "gce_project": "neo-narrative",
      "gce_private_ip": "2.2.2.2",
      "gce_instance_name": "TEST-02",
      "cluster": "narrative-sandbox-cluster"
    },
    "annotations": {
      "identifier": "TEST-02",
      "message": "TESTING message here.",
      "runbook_url": "https://github.com/NarrativeCompany/narrative-infrastructure/wiki/Alertmanager-Runbooks#systemdfailed"
    },
    "generatorURL": "https://prometheus.narrative.cloud/graph?g0.expr=node_systemd_unit_state%7Bstate%3D%22failed%22%7D+%3D%3D+1&g0.tab=1"
  }
]'

alert2='[
  {
    "status": "firing",
    "labels": {
     "alertname": "TESTNeoBlockRateLow",
      "state": "failed",
      "severity": "warning",
      "name": "TEST2.service",
      "job": "neo-narrative-prmetheus-node-exporter",
      "instance": "2.2.2.2:9243",
      "gce_public_ip": "22.22.22.22",
      "gce_project": "neo-narrative",
      "gce_private_ip": "2.2.2.2",
      "gce_instance_name": "TEST-02",
      "cluster": "neo"
    },
    "annotations": {
      "identifier": "TEST-02",
      "message": "TESTING message here.",
      "runbook_url": "https://github.com/NarrativeCompany/narrative-infrastructure/wiki/Alertmanager-Runbooks#systemdfailed"
    },
    "generatorURL": "https://prometheus.narrative.cloud/graph?g0.expr=node_systemd_unit_state%7Bstate%3D%22failed%22%7D+%3D%3D+1&g0.tab=1"
  }
]'

kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster

POD=$(kubectl -n it get pods | grep alertmanager | awk '{ print $1 }')
kubectl -n it port-forward ${POD} 9093:9093 &
FORWARDER=$!
echo "$FORWARDER"

sleep 3

curl -XPOST -d"$alert" 'http://localhost:9093/api/v1/alerts'
curl -XPOST -d"$alert_webhook_2" 'http://localhost:9093/api/v1/alerts'

kill ${FORWARDER}
