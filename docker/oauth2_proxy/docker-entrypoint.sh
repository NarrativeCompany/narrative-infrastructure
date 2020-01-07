#!/bin/bash
set -e

# first arg is `-f` or `--some-option`
if [ "${1:0:1}" = '-' ]; then
    set -- oauth2_proxy "$@"
fi

if [ ! -f /conf/oauth2_proxy.conf ]; then
    # if undefined, populate selected environment variables with sane defaults
    : ${OAUTH2_PROXY_EMAIL_DOMAIN="*"}
    : ${OAUTH2_PROXY_HTTP_ADDRESS="0.0.0.0:4180"}
    : ${OAUTH2_PROXY_COOKIE_SECRET="$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1 | md5sum)"}

    # available config variables '
    VARS="
	approval-prompt
	authenticated-emails-file
	azure-tenant
	basic-auth-password
	client-id
	client-secret
	config
	cookie-domain
	cookie-expire
	cookie-httponly
	cookie-name
	cookie-refresh
	cookie-secret
	cookie-secure
	custom-templates-dir
	display-htpasswd-form
	email-domain
	github-org
	github-team
	google-admin-email
	google-group
	google-service-account-json
	htpasswd-file
	http-address
	https-address
	login-url
	pass-access-token
	pass-basic-auth
	pass-host-header
	profile-url
	provider
	proxy-prefix
	redeem-url
	redirect-url
	resource
	request-logging
	scope
	signature-key
	skip-auth-regex
	skip-provider-button
	tls-cert
	tls-key
	upstream
	validate-url"
    for var in ${VARS}; do
	# config file variable names are with _ instead of -
	var="$(echo ${var} | sed -e 's/-/_/g')"
	# convert config variable name to bash compatible variable name
	# e.g. "approval-prompt" becomes "OAUTH2_PROXY_APPROVAL_PROMPT"
	env_var="OAUTH2_PROXY_$(echo ${var} | tr '[:lower:]' '[:upper:]')"
	# test for environment variable existence
	if [ ! -z ${!env_var+x} ]; then
	    # list values need to be treated especially
	    if [ "${var}" == "upstream" ] \
	    || [ "${var}" == "email_domain" ]; then
		# plural and list
		echo "${var}s = [ " >> /conf/oauth2_proxy.conf
		# disable * expansion
		set -f
		for v in ${!env_var}; do
		    echo "  \"${v}\"," >> /conf/oauth2_proxy.conf
		done
		set +f
		echo "]" >> /conf/oauth2_proxy.conf
	    else
		# unfortunately ${!var} is only available in bash and not sh
		# that's why the alpine container installs bash as runtime dependency
		echo "${var} = \"${!env_var}\"" >> /conf/oauth2_proxy.conf
	    fi
	fi
    done
    cat /conf/oauth2_proxy.conf
fi

exec "$@"

