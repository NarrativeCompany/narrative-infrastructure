# neo-python

This project builds the neo-python Docker image, from the CityOfZion code - <https://github.com/CityOfZion/neo-python>.

Note that this should be handled by the Jenkins job - <https://jenkins.narrative.cloud/job/neo_python_docker_build/>.

# Building

To build locally, run:

```
./build_docker.sh
```

Note that this will attach the following label to the Docker image:

```
GIT_SHA=$GIT_SHA
```

This is the Git SHA of the last commit for the neo-python project.
