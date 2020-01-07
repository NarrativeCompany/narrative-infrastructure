# Custom JNLP Jenkins Docker Image

Should probably consider using this much more modern Docker image: <https://hub.docker.com/r/jenkins/jnlp-slave/>.

To build:

```
docker build -t narrativecompany/jnlp:latest .
docker push narrativecompany/jnlp:latest
```

Configure Jenkins to use this agent for preferred jobs.
