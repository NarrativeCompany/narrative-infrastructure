#!/bin/bash

PARAM=${1}

function usage {
  echo "  Usage: $0 [jenkins version]

  Example: $0 2.150

  Visit https://hub.docker.com/r/jenkins/jenkins/tags/ or run $0 -v to view the latest versions.
"
exit 1;
}

function parse_commandline {
  if [ -z "${PARAM}" ]; then
    usage
    exit 1;
  elif [[ ( ${PARAM} == "-h" ) || ${PARAM} == "--help" ]]; then
    usage
    exit 1;
  elif [[ ( ${PARAM} == "-v" ) || ${PARAM} == "--version" ]]; then
    curl -s https://hub.docker.com/v2/repositories/jenkins/jenkins/tags/?page_size=18 | jq --raw-output '.results[] .name'
    exit 1;
  fi
}

function check_version {
  # Get a list of recent versions and see if the version passed in matches one.
  JENKINS_VERSION=$(curl -s https://hub.docker.com/v2/repositories/jenkins/jenkins/tags/?page_size=18 | jq --raw-output '.results[] .name' | grep ${PARAM}$)
  # If the param doesn't match a recent jenkins version display usage and exit.
  if [ "${PARAM}" != "${JENKINS_VERSION}" ]; then
    echo ""
    echo "  Version not found..."
    echo ""
    usage
  fi
}

parse_commandline $1
check_version

echo "Setting context to gke_informationtechnology-205813_us-east1_it-cluster"
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster
echo ""
echo "Listing jenkins pod in jenkins namespace"
kubectl -n jenkins get pods | grep jenkins | grep -v webhook
echo ""
echo "Setting jenkins replicas to 0"
kubectl -n jenkins patch deployment jenkins -p '{"spec":{"replicas": 0}}'
echo ""
echo "Waiting for jenkins pod to terminate"
while kubectl -n jenkins get pods | grep jenkins | grep -v webhook; do sleep 1; done
echo ""
echo "Updating jenkins deployment with new image version: ${JENKINS_VERSION}"
kubectl -n jenkins set image deployment/jenkins master=jenkins/jenkins:${JENKINS_VERSION}
echo ""
echo "Setting jenkins replicas to 1"
kubectl -n jenkins patch deployment jenkins -p '{"spec":{"replicas": 1}}'
echo ""

