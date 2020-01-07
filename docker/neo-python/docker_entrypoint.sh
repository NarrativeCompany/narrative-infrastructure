#!/bin/bash
source venv/bin/activate
if [ "${1}" = "-t" ]; then
  # TestNet
  python3.7 /opt/neo-python/neo/bin/api_server.py --testnet --maxpeers 10 --disable-stderr --host 127.0.0.1 --port-rpc 10332
elif [ "${1}" = "-s" ]; then
  # Sleep
  sleep infinity
else
  # MainNet
  python3.7 /opt/neo-python/neo/bin/api_server.py --mainnet --maxpeers 10 --disable-stderr --host 127.0.0.1 --port-rpc 10332
fi
