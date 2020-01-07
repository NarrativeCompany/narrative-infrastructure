FROM narrativecompany/neo-python:v0.9.1

LABEL GIT_SHA=$GIT_SHA

# Package install
USER root
RUN mkdir -p /var/lib/apt/lists/partial
RUN apt-get update && apt-get install -y \
    wget

# APT cleanup to reduce image size
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*

# # Clone and setup
RUN mkdir -p /opt/tokensale-neo-smartcontract
COPY tokensale-neo-smartcontract /opt/tokensale-neo-smartcontract
RUN ln -s /opt/tokensale-neo-smartcontract/util/ /opt/neo-python/neo/contrib/narrative
RUN chown -R neo-user:neo-user /opt/tokensale-neo-smartcontract

# Create docker_entrypoint.sh
USER root
COPY docker_entrypoint.sh /docker_entrypoint.sh
RUN chmod a+x /docker_entrypoint.sh

# Create neo_python_sync.sh
COPY neo_python_sync.sh /neo_python_sync.sh
RUN chmod a+x /neo_python_sync.sh

USER neo-user
RUN python3.7 -m venv venv
RUN /bin/bash -c "source venv/bin/activate && pip install -e ."

# Entrypoint
ENTRYPOINT ["/docker_entrypoint.sh"]
CMD ["-m"]
