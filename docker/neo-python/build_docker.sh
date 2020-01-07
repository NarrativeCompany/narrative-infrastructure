#!/bin/bash
set -e

if [ ! -d neo-python ]; then 
  echo "Cloning directory..."
  git clone https://github.com/CityOfZion/neo-python.git
  cd neo-python
  GIT_SHA_MASTER=$(git show-ref -s refs/heads/master)
else
  echo "Pulling neo-python..."
  cd neo-python
  git checkout master
  git pull
  GIT_SHA_MASTER=$(git show-ref -s refs/heads/master)
fi

echo "Get latest tag..."
GIT_TAG=$(git tag | tail -n 1)

git checkout tags/${GIT_TAG} -b ${GIT_TAG} || git checkout ${GIT_TAG}

GIT_SHA_TAG=$(git show-ref -s refs/tags/${GIT_TAG})

echo "${GIT_SHA_MASTER}"
echo "${GIT_SHA_TAG}"

if [ "${GIT_SHA_MASTER}" != "${GIT_SHA_TAG}" ]; then
  echo "master Git SHA does not match the latest Git tag SHA. Exiting..."
  exit 1
fi

cd ..

echo "Building neo-python Docker image..."
echo "Git SHA: ${GIT_SHA_TAG}"
echo "Git tag: ${GIT_TAG}"
docker build -t narrativecompany/neo-python:${GIT_TAG} --build-arg GIT_SHA_TAG=${GIT_SHA_TAG} --build-arg GIT_TAG=${GIT_TAG} .
docker tag narrativecompany/neo-python:${GIT_TAG} narrativecompany/neo-python:latest
# docker login -u "${USERNAME}" -p "${PASSWORD}"
# docker push narrativecompany/neo-python:${GIT_TAG}
# docker push narrativecompany/neo-python:latest

echo  "Removing neo-python Github repo..."
# rm -rf neo-python
