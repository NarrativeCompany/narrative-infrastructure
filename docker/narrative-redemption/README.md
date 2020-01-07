# Overview

Documentation on the narrative-redemption Docker image.

# Build

To build, run the build_docker script:

```
./build_docker.sh
```

Then retag and push (change date tag to current date)

```
docker tag narrativecompany/narrative-redemption:latest narrativecompany/narrative-redemption:2019-07-03-01
docker login -u "${USERNAME}" -p "${PASSWORD}"
docker push narrativecompany/narrative-redemption:2019-07-03-01
docker push narrativecompany/narrative-redemption:latest
docker logout
```
