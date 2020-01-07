# jenkins-webhook

This handles incoming webhooks from Github and redirects them to Jenkins. Based off the awesome <https://github.com/carlos-jenkins/python-github-webhooks>.

The NarrativeCompany-wide Github webhook is configured to POST the Github messages to this Kubernetes pod, which then sends the POST to Jenkins. A nice way to keep Jenkins private.

# Build the Docker image

To build and push:

```
docker build -t narrativecompany/jenkins-webhook:latest .
docker push narrativecompany/jenkins-webhook:latest
```

# Deploy to GKE

Verify that you are using the GKE context:


Create the Github and Jenkins secrets (found in 1Password `jenkins generic-webproxy github kubernetes secret`):

```
# found in 1password
kubectl -n jenkins create secret generic github-secret --from-literal=SECRET=<SECRET_IN_1PASSWORD>
kubectl -n jenkins create secret generic jenkins-token --from-literal=SECRET=<SECRET_IN_1PASSWORD>
```
