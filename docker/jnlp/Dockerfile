FROM gcr.io/cloud-solutions-images/jenkins-k8s-slave:latest

RUN apt-get update && apt-get upgrade -y
RUN apt-get update && apt-get -y install python-pip
RUN pip install ansible
RUN pip install apache-libcloud
RUN gcloud components update
RUN gcloud components install beta
RUN apt-get autoremove -y
RUN rm -rf /var/lib/apt/lists/*
