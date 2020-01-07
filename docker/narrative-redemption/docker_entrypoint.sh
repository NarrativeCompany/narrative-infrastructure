#!/bin/bash

# Turn on bash's job control
set -m

OPTION="$1"

# If we want to use TestNet.
if [ "${OPTION}" = "-t" ]; then
  # Bootstrap Test and Test_Notif chains if they do not exist.
  if [ ! -d "/home/neo-user/.neopython/Chains/SC234" ]; then

    cd /home/neo-user/.neopython
    wget https://neo-python-chain.narrative.org/Test_latest.tar.gz
    tar vxzf Test_latest.tar.gz
    rm -f /home/neo-user/.neopython/Test_latest.tar.gz
    wget https://neo-python-chain.narrative.org/Test_Notif_latest.tar.gz
    tar vxzf Test_Notif_latest.tar.gz
    rm -f /home/neo-user/.neopython/Test_Notif_latest.tar.gz
    chown -R neo-user:neo-user /home/neo-user/.neopython/Chains

  fi
# Else we are using MainNet.
else
  # Download Main and Main_Notif chains if they do not exist.
  if [ ! -d "/home/neo-user/.neopython/Chains/Main" ]; then

    cd /home/neo-user/.neopython
    wget https://neo-python-chain.narrative.org/Main_latest.tar.gz
    tar vxzf Main_latest.tar.gz
    rm -f /home/neo-user/.neopython/Main_latest.tar.gz
    wget https://neo-python-chain.narrative.org/Main_Notif_latest.tar.gz
    tar vxzf Main_Notif_latest.tar.gz
    rm -f /home/neo-user/.neopython/Main_Notif_latest.tar.gz
    chown -R neo-user:neo-user /home/neo-user/.neopython/Chains

  fi
fi

# Start neo-python to get the blockchain up to date.
/neo_python_sync.sh ${OPTION} &

# Sleep forever so that the docker container keeps running.
sleep infinity
