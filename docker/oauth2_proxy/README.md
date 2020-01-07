# oauth2_proxy

This project builds a Docker image for [oauth2_proxy](https://github.com/bitly/oauth2_proxy).  It's particularly made to be used with Kubernetes. Based off the Dockerfile found [here](https://hub.docker.com/r/machinedata/oauth2_proxy/).

# To Build

```
docker build -t oauth2_proxy:latest .
docker tag oauth2_proxy:latest narrativecompany/oauth2_proxy:latest
docker push narrativecompany/oauth2_proxy:latest
```

Note, this will require AWS ECR login.

# Environment Variables

This Docker image can take in a list of `VARS` as environment variables.  This can be seen in the `docker-entrypoint.sh` file. Some useful environment variables are:

```
OAUTH2_PROXY_CLIENT_ID
OAUTH2_PROXY_CLIENT_SECRET
OAUTH2_PROXY_COOKIE_SECRET
OAUTH2_PROXY_PROVIDER
```


