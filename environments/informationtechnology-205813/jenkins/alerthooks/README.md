# webhook-alertmanager

This handles incoming webhooks from Github and redirects them to Jenkins. Based off the awesome <https://github.com/carlos-jenkins/python-github-webhooks>.

# Build the Docker image

To build and push:

```
docker build -t narrativecompany/jenkins-webhook:alerthooks-latest .
docker push narrativecompany/jenkins-webhook:alerthooks-latest
```

# Deploy to GKE

Requires the jenkins-webhook secrets.

```
kubectl apply -f alerthooks-deployment.yml
```

