#!/bin/bash

# Turn on bash's job control
set -m

OPTION="$1"

# Start neo-python to get the blockchain up to date.
cd /opt/neo-python
source venv/bin/activate

if [ "${OPTION}" = "-t" ]; then
  # TestNet
  python3.7 /opt/neo-python/neo/bin/api_server.py --testnet --maxpeers 10 --disable-stderr --host 127.0.0.1 --port-rpc 10332 &
else
  # MainNet
  python3.7 /opt/neo-python/neo/bin/api_server.py --mainnet --maxpeers 10 --disable-stderr --host 127.0.0.1 --port-rpc 10332 &
fi
