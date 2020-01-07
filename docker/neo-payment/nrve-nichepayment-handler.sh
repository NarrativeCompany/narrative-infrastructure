#!/bin/bash

cd /opt/neo-python

# bl: below steps from: https://github.com/CityOfZion/neo-python#virtual-environment

#python3.6 -m venv venv
source venv/bin/activate
# bl: these only need to be done once, so not including every time.
#pip install -U setuptools pip wheel
#pip install -e .
# python neo/contrib/narrative/nrve-niche-payment-handler.py >> /var/log/nrve-niche-payment-handler.log 2>&1 &
python neo/contrib/narrative/nrve-niche-payment-handler.py

# bl: if you want to just sync the blockchain from neo-python's prompt.py:
#python prompt.py -m
