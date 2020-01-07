# -*- coding: utf-8 -*-
import base64
import logging
from sys import stdout,stderr, hexversion

import hmac
from hashlib import sha1
from json import loads, dumps
from subprocess import Popen, PIPE
from tempfile import mkstemp
import os
from os import access, X_OK, remove, fdopen
from os.path import isfile, abspath, normpath, dirname, join, basename

import requests
from ipaddress import ip_address, ip_network
from flask import Flask, request, abort


application = Flask(__name__)

# https://api.slack.com/methods/users.list/test
GIT_USER_MAP = { "bonovoxly": "<@UALFALVHV>",
                "brianlenz": "<@U6U06HHC4>",
                "bsantare": "<@UBGBGKAH1>",
                "brooxmagnetic": "<@UAFBZPRPB>",
                "Jeff-Narrative": "<@UALN1163Y>",
                "JonmarkWeber": "<@UALQBPWCE>",
                "lorilhope": "<@U6UQCSETW>",
                "michaelf318is": "<@U6TFTNUEM>",
                "paulonarrative": "<@U9QH69YR3>",
                "platypusrex": "<@UAQDZRRQF>",
                "PWAlessi": "<@UB11V5HDZ>",
                "RosemaryONeill1": "<@U6PJ6USTU>",
                "ted-oneill": "<@U6NUAHPEU>"
 }


@application.route('/', methods=['GET', 'POST'])
def index():
    """
    Main WSGI application entry.
    """
    log = logging.getLogger('werkzeug')
    log.setLevel(logging.INFO)

    path = normpath(abspath(dirname(__file__)))

    header_ip = request.headers.get('X-Real-IP')
    header_forwarded_for = request.headers.get('X-Forwarded-For')
    log.info(request.headers)
    log.info('X-Real-IP: {}'.format(header_ip))
    log.info('X-Forwarded-For: {}'.format(header_ip))

    # Defaults for the Bearer token. btoken is from the environment
    bearer = 'default'
    btoken = ''

    # Only POST is implemented
    if request.method != 'POST':
        abort(501)

    # Enforce secret
    if os.environ["BEARERTOKEN"]:
        bearer = os.environ["BEARERTOKEN"]

    bearer_token = request.headers.get('Authorization')
    if bearer_token is not None:
        log.info('FOUND BEARER TOKEN!')
        junk, btoken = bearer_token.split()

    if btoken == bearer:
        log.info('Bearer token checks out....')
    else:
        log.error('BEARER TOKEN DOESNT CHECK OUT... GAME OVER MAN GAME OVER.')
        abort(501)

    # Gather data
    log.info('Gathering data...')
    try:
        payload = request.get_json()
    except Exception:
        log.warning('Request parsing failed')
        abort(400)

    # these IF checks are just extra credit....
    if btoken == bearer:
        log.info('Parsing Alertmanager message...')
        # get alerts
        alerts_list_jenkins = []
        for each in payload['alerts']:
            alerts_jenkins = {}
            if each['status'] == 'firing':
                log.info('Found alerts firing....')
                log.info('Identifier: %s', each['annotations']['identifier'])
                for key in each['labels']:
                    alerts_jenkins[key] = each['labels'][key]
            alerts_list_jenkins.append(alerts_jenkins)

    if btoken == bearer:
        token = os.environ["TOKEN"]
        url = "".join(['http://jenkins:8080/generic-webhook-trigger/invoke?token=', token])
        for each in alerts_list_jenkins:
            log.info('Alertmanager Payload:\n{}'.format(dumps(each)))
            headers = {'Content-type': 'application/json', 'Accept': 'text/plain', 'User-Agent': 'jenkins-webhook-python'}
            r = requests.post(url, data=dumps(each), headers=headers)
    else:
        log.warning('Something went wrong.... Oh dear.')
        abort(400)

    return '200 OK'

if __name__ == '__main__':
    application.run(debug=True, host='0.0.0.0')
