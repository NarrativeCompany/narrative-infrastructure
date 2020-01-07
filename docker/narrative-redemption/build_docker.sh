#!/bin/bash
set -e

DATE=$(date +%Y-%m-%d)

if [ ! -d tokensale-neo-smartcontract ]; then 
  echo "Cloning directory..."
else
  echo "Deleting directory to clone repository..."
  rm -rf ./tokensale-neo-smartcontract
fi

git clone https://github.com/NarrativeCompany/tokensale-neo-smartcontract

cd tokensale-neo-smartcontract
GIT_SHA=$(git --no-pager log -n 1 --pretty=format:"%H")
cd ..

echo "Building narrative-redemption Docker image..."
echo "Git SHA: ${GIT_SHA}"
docker build -t narrativecompany/narrative-redemption:latest --label GIT_SHA=${GIT_SHA} .
# docker tag narrativecompany/narrative-redemption:latest narrativecompany/narrative-redemption:${DATE}
# docker login -u "${USERNAME}" -p "${PASSWORD}"
# docker push narrativecompany/narrative-redemption:${DATE}
# docker push narrativecompany/narrative-redemption:latest
# docker logout

echo  "Removing tokensale-neo-smartcontract Github repo..."
rm -rf tokensale-neo-smartcontract
