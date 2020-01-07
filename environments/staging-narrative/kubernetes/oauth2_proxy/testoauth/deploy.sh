#!/bin/bash
kubectl create -f cert.yml 
kubectl create configmap testoauth-index --from-file=./index.html --from-file=./50x.html
kubectl create -f testoauth-deployment.yml
kubectl create -f service.yml
kubectl create -f ingress.yml
