FROM narrativecompany/neo-python:v0.8.4

LABEL GIT_SHA=$GIT_SHA

# # Clone and setup
RUN mkdir -p /opt/neo-python/tokensale-neo-smartcontract
COPY tokensale-neo-smartcontract /opt/neo-python/tokensale-neo-smartcontract
RUN ln -s /opt/neo-python/tokensale-neo-smartcontract/util/ /opt/neo-python/neo/contrib/narrative

# Create nrve-nichepayment-handler.sh
USER root
COPY nrve-nichepayment-handler.sh /nrve-nichepayment-handler.sh
RUN chmod a+x /nrve-nichepayment-handler.sh

USER neo-user

RUN echo "pymysql==0.8.0" >> /opt/neo-python/requirements.txt
RUN python3 -m venv venv
RUN /bin/bash -c "source venv/bin/activate && pip install -e ."

# Entrypoint
ENTRYPOINT ["/nrve-nichepayment-handler.sh"]
