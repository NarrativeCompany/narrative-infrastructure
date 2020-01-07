#!/bin/bash
set -e
git clone https://github.com/NarrativeCompany/neo-mon.git
cd neo-mon

git checkout -b narrative-mon origin/narrative-mon

cp ../Dockerfile* ./

docker build -t narrativecompany/neo-mon-build:latest -f Dockerfile-build .

docker run --name neo-mon-build -d --rm -v ${PWD}:/opt/neo-mon --entrypoint=/bin/bash narrativecompany/neo-mon-build:latest -c "sleep infinity"

docker exec neo-mon-build /opt/neo-mon-build/node_modules/.bin/gulp

docker exec neo-mon-build /bin/bash -c "cp -r /opt/neo-mon-build/docs/* /opt/neo-mon/docs/"

docker stop neo-mon-build

docker build -t narrativecompany/neo-mon:latest .

cd ../

rm -rf neo-mon
