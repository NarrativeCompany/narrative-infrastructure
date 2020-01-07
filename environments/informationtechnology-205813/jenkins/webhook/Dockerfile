FROM python:3.6-stretch

WORKDIR /opt

COPY Dockerfile /opt

COPY requirements.txt /opt

RUN pip install -r requirements.txt

COPY webhooks.py /opt

RUN mkdir -p /opt/conf

COPY config.json.sample /opt/conf/config.json

EXPOSE 5000

CMD ["python", "webhooks.py"]
