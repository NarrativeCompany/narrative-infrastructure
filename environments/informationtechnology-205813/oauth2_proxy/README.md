# oauth2_proxy Kubernetes

This project deploys the [oauth2_proxy](https://github.com/bitly/oauth2_proxy) reverse proxy to Kubernetes. The Docker image is found in `../docker`.

# Requirements

- You will need a Google API OAuth client ID. This is for the oauth2_proxy service to redirect the user to accounts.google.com for authentication. This can be configured [here](https://console.developers.google.com). The [oauth2_proxy Github page has good information as well](https://github.com/bitly/oauth2_proxy#google-auth-provider).
- You will need to have A Kubernetes secret in the kube-system namespace. Example to create that:
- You will need to create the Docker image `registry-creds`, in order to access the AWS ECR registry.

# registry-creds

Clone the repo:

```
git clone https://github.com/upmc-enterprises/registry-creds.git
```

```
kubectl --namespace default create secret generic oauth --from-literal=OAUTH2_PROXY_CLIENT_ID=your_client_id_from_google --from-literal=OAUTH2_PROXY_CLIENT_SECRET=your_client_secret_from_google --from-literal=OAUTH2_PROXY_COOKIE_SECRET=random_base64_encoded_value
```

- Edit the following line and insert your domain, such as "example.org":

```
        - --email-domain=<YOUREMAILDOMANHERE>
```

# Deployment

To deploy:

```
kubectl create -f oauth2_proxy.yml
```

You can verify the status by running:

```
kubectl -n kube-system get pods
```

# Using an email auth file

If using the email auth file deployment, you will need to create a volume mount that is the email auth file, containing the list of email addresses who are allowed access. Example yml file provided.
