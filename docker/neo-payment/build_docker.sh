#!/bin/bash
set -e

NEO_PAYMENT_BRANCH="${1:-master}"

if [ ! -d tokensale-neo-smartcontract ]; then 
  echo "Cloning directory..."
else
  echo "Deleting directory to clone repository..."
  rm -rf ./tokensale-neo-smartcontract
fi

git clone https://github.com/NarrativeCompany/tokensale-neo-smartcontract
cd tokensale-neo-smartcontract
echo "pymysql==0.8.0" >> requirements.txt

if [ "${NEO_PAYMENT_BRANCH}" == 'master' ]; then
  echo "Using master branch..."
else
  git checkout -b ${NEO_PAYMENT_BRANCH} origin/${NEO_PAYMENT_BRANCH}
fi

GIT_SHA=$(git --no-pager log -n 1 --pretty=format:"%H")

cd ..

echo "Building neo-payment Docker image..."
echo "Git SHA: ${GIT_SHA}"
docker build -t narrativecompany/neo-payment:latest --label GIT_SHA=${GIT_SHA} .

echo  "Removing tokensale-neo-smartcontract Github repo..."
rm -rf tokensale-neo-smartcontract
