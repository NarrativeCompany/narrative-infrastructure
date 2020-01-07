# -*- coding: utf-8 -*-
#
# Copyright (C) 2014, 2015, 2016 Carlos Jenkins <carlos@jenkins.co.cr>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

import base64
import logging
from sys import stdout,stderr, hexversion
# logging.basicConfig(stream=stdout)

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
    # ch = logging.StreamHandler()
    # ch.setLevel(logging.INFO)
    #log.addHandler(ch)

    path = normpath(abspath(dirname(__file__)))

    header_ip = request.headers.get('X-Real-IP')
    header_forwarded_for = request.headers.get('X-Forwarded-For')
    log.info(request.headers)
    log.info('X-Real-IP: {}'.format(header_ip))
    log.info('X-Forwarded-For: {}'.format(header_ip))

    # Only POST is implemented
    if request.method != 'POST':
        abort(501)

    # Load config
    with open(join(path, 'conf/config.json'), 'r') as cfg:
        config = loads(cfg.read())

    # hooks = config.get('hooks_path', join(path, 'hooks'))

    # Allow Github IPs only
    if config.get('github_ips_only', True):
        src_ip = ip_address(
            u'{}'.format(request.access_route[0])  # Fix stupid ipaddress issue
        )
        whitelist = requests.get('https://api.github.com/meta').json()['hooks']

        for valid_ip in whitelist:
            if src_ip in ip_network(valid_ip):
                break
        else:
            log.error('IP {} not allowed'.format(
                src_ip
            ))
            abort(403)

    # Enforce secret
    if os.environ["SECRET"]:
        secret = os.environ["SECRET"]
    else:
        secret = config.get('enforce_secret', '')
    if secret:
        # Only SHA1 is supported
        header_signature = request.headers.get('X-Hub-Signature')
        if header_signature is None:
            log.error('header_signature missing.')
            abort(403)

        sha_name, signature = header_signature.split('=')
        if sha_name != 'sha1':
            log.error('sha not sha1.')
            abort(501)

        # HMAC requires the key to be bytes, but data is string
        secret_bytes= bytes(secret, 'latin-1')
        mac = hmac.new(secret_bytes, request.data, digestmod='sha1')

        # Python prior to 2.7.7 does not have hmac.compare_digest
        if hexversion >= 0x020707F0:
            if not hmac.compare_digest(str(mac.hexdigest()), str(signature)):
                log.error('hmac.compare_digest failed.')
                log.error(str(mac.hexdigest()))
                log.error(str(signature))
                abort(403)
        else:
            # What compare_digest provides is protection against timing
            # attacks; we can live without this protection for a web-based
            # application
            if not str(mac.hexdigest()) == str(signature):
                log.error('mac.hexdigest not equal to string signature.')
                abort(403)

    # Implement ping
    event = request.headers.get('X-GitHub-Event', 'ping')
    if event == 'ping':
        return dumps({'msg': 'pong'})

    # Gather data
    try:
        payload = request.get_json()
    except Exception:
        log.warning('Request parsing failed')
        abort(400)

    # Determining the branch is tricky, as it only appears for certain event
    # types an at different levels
    branch = None
    try:
        # Case 1: a ref_type indicates the type of ref.
        # This true for create and delete events.
        if 'ref_type' in payload:
            if payload['ref_type'] == 'branch':
                branch = payload['ref']

        # Case 2: a pull_request object is involved. This is pull_request and
        # pull_request_review_comment events.
        elif 'pull_request' in payload:
            # This is the TARGET branch for the pull-request, not the source
            # branch
            branch = payload['pull_request']['base']['ref']

        elif event in ['push']:
            # Push events provide a full Git ref in 'ref' and not a 'ref_type'.
            branch = payload['ref'].split('/', 2)[2]
            branch_origin = "origin/{}".format(branch)
            # add custom fields
            payload['branch_origin'] = branch_origin
            if payload['pusher']['name'] in GIT_USER_MAP:
                print("Slack user found: {}".format(payload['pusher']['name']))
                payload['slack_user'] = GIT_USER_MAP[payload['pusher']['name']]
            else:
                print("Slack user not found: {}".format(payload['pusher']['name']))
                payload['slack_user'] = payload['pusher']['name']

    except KeyError:
        # If the payload structure isn't what we expect, we'll live without
        # the branch name
        pass

    # All current events have a repository, but some legacy events do not,
    # so let's be safe
    name = payload['repository']['name'] if 'repository' in payload else None

    meta = {
        'name': name,
        'branch': branch,
        'event': event
    }
    log.info('Metadata:\n{}'.format(dumps(meta)))

    # Skip push-delete
    if event == 'push' and payload['deleted']:
        log.info('Skipping push-delete event for {}'.format(dumps(meta)))
        return dumps({'status': 'skipped'})

    # # Possible hooks
    # scripts = []
    # if branch and name:
    #     scripts.append(join(hooks, '{event}-{name}-{branch}'.format(**meta)))
    # if name:
    #     scripts.append(join(hooks, '{event}-{name}'.format(**meta)))
    # scripts.append(join(hooks, '{event}'.format(**meta)))
    # scripts.append(join(hooks, 'all'))

    # log the paylod
    log.info('Payload:\n{}'.format(dumps(payload)))

    if os.environ["TOKEN"]:
        token = os.environ["TOKEN"]
        url = "".join(['http://jenkins:8080/generic-webhook-trigger/invoke?token=', token])
    else:
        url = config.get('jenkins_url', 'http://jenkins:8080/generic-webhook-trigger/invoke')
    headers = {'Content-type': 'application/json', 'Accept': 'text/plain', 'User-Agent': 'jenkins-webhook-python'}
    r = requests.post(url, data=dumps(payload), headers=headers)
    # Ignoring this section
    # # Check permissions
    # scripts = [s for s in scripts if isfile(s) and access(s, X_OK)]
    # if not scripts:
    #     return dumps({'status': 'nop'})

    # # Save payload to temporal file
    # osfd, tmpfile = mkstemp()
    # with fdopen(osfd, 'w') as pf:
    #     pf.write(dumps(payload))

    # # Run scripts
    # ran = {}
    # for s in scripts:

    #     proc = Popen(
    #         [s, tmpfile, event],
    #         stdout=PIPE, stderr=PIPE
    #     )
    #     stdout, stderr = proc.communicate()

    #     ran[basename(s)] = {
    #         'returncode': proc.returncode,
    #         'stdout': stdout.decode('utf-8'),
    #         'stderr': stderr.decode('utf-8'),
    #     }

    #     # Log errors if a hook failed
    #     if proc.returncode != 0:
    #         logging.error('{} : {} \n{}'.format(
    #             s, proc.returncode, stderr
    #         ))

    # # Remove temporal file
    # remove(tmpfile)

    info = config.get('return_scripts_info', False)
    if not info:
        return dumps({'status': 'done'})

    # output = dumps(ran, sort_keys=True, indent=4)
    # logging.info(output)
    return '200 OK'


if __name__ == '__main__':
    application.run(debug=True, host='0.0.0.0')
