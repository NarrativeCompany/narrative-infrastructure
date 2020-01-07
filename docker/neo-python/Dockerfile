FROM ubuntu:18.04

ARG GIT_SHA_TAG
ARG GIT_TAG

LABEL GIT_SHA_TAG=$GIT_SHA_TAG
LABEL GIT_TAG=$GIT_TAG



RUN apt-get update && apt-get install -y \
    python3.7 \
    python3.7-dev \
    python3.7-venv \
    python3-pip \
    libleveldb-dev \
    libssl-dev

# APT cleanup to reduce image size
RUN rm -rf /var/lib/apt/lists/*

# Create docker_entrypoint.sh
COPY docker_entrypoint.sh /docker_entrypoint.sh
RUN chmod a+x /docker_entrypoint.sh

# # Clone and setup
RUN mkdir -p /opt/neo-python
COPY neo-python /opt/neo-python

WORKDIR /opt/neo-python
RUN useradd neo-user -m -s /bin/bash
RUN chown -R neo-user:neo-user /opt/neo-python
USER neo-user
RUN mkdir -p /home/neo-user/.neopython

RUN python3.7 -m venv venv
RUN /bin/bash -c "source venv/bin/activate && pip install -e ."
# TODO - workaround for https://github.com/CityOfZion/neo-python/issues/485
# RUN /bin/bash -c "source venv/bin/activate && pip uninstall -y pycrypto && pip uninstall -y pycryptodome && pip install pycryptodome"

# # Configure Volumes
# VOLUME ["/home/neo-user/.neopython"]

# Entrypoint
ENTRYPOINT ["/docker_entrypoint.sh"]
CMD ["-m"]
