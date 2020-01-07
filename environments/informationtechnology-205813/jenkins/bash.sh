#!/bin/bash
if [ $(env |grep GIT_BRANCH | awk -F'=' ' { print $NF }') == "dev-pipeline" ]; then
  echo "here"
elif [ "${GIT_BRANCH}" == "dev" ]; then
  echo "there"
else
  echo "done"
fi
