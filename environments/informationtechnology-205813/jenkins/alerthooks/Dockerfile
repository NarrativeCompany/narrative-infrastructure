FROM python:3.6-stretch

WORKDIR /opt

COPY Dockerfile /opt

COPY requirements.txt /opt

RUN pip install -r requirements.txt

COPY alerthooks.py /opt

RUN mkdir -p /opt/conf

EXPOSE 5000

CMD ["python", "alerthooks.py"]
