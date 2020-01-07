#!/bin/bash
kubectl delete -f ingress.yml
kubectl delete -f service.yml
kubectl delete -f testoauth-deployment.yml
kubectl delete configmap testoauth-index
kubectl delete -f cert.yml 
