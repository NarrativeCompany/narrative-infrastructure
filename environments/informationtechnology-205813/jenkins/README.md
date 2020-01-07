# jenkins

The deployment of Jenkins to Google Kubernetes Engine.

# Requirements

- The IT Google Kubernetes Engine stack deployed.
- Access to the GKE cluster.
- Other components, such as nginx-ingress, oauth2_proxy, and cert-manager.

# Access to the GKE cluster

Command to connect to the informationtechnology kubernetes it-cluster:
```
gcloud beta container clusters get-credentials it-cluster --region us-east1 --project informationtechnology-205813
kubectl config use-context gke_informationtechnology-205813_us-east1_it-cluster
```

# Deployment

Create the namespace:

```
kubectl create namespace jenkins
```

Create the `options` file, which contains the secrets for Jenkins. It should contain the following:

```
--argumentsRealm.passwd.jenkins=<1PASSWORD-PASSWORD> --argumentsRealm.roles.jenkins=admin
```

Deploy Jenkins:

```
gcloud compute images create jenkins-home-image --source-uri https://storage.googleapis.com/solutions-public-assets/jenkins-cd/jenkins-home-v3.tar.gz
gcloud compute disks create jenkins-home --image jenkins-home-image --zone us-east-1c
kubectl create namespace jenkins
kubectl -n jenkins create secret generic jenkins --from-file options
kubectl create -f jenkins-rbac.yml
kubectl create -f jenkins-deployment.yml
kubectl create -f jenkins-service.yml
kubectl create -f jenkins-narrative-cloud-certificate.yml
kubectl create -f jenkins-narrative-cloud-ingress.yml
```

Delete the `options` file.

# Jenkins Customizations

Plugins installed:

- Slack
- JobConfigHistory
- Generic Webhook
- Jenkins Jobs DSL

Configure the Seed job to read the DSL groovy files in the Jenkins jobs folder.

# Google Compute Worker

To enable Jenkins to create/destroy instances in Google Compute:

```
gcloud projects add-iam-policy-binding jenkins-workers --member serviceAccount:jenkins-workers@informationtechnology-205813.iam.gserviceaccount.com --role roles/iam.serviceAccountUser
gcloud projects add-iam-policy-binding jenkins-workers --member serviceAccount:jenkins-workers@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.instanceAdmin
```

To enable Jenkins to list instances in a project (used for Ansible dynamic inventory):

```
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role organizations/149049775531/roles/computereadonly
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role organizations/149049775531/roles/computereadonly
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role organizations/149049775531/roles/computereadonly
```

To enable Jenkins to create/delete Google Cloud SQL backups:

```
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudsql.admin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudsql.admin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudsql.admin
```

To enable Jenkins to create/delete VM disk snapshots:

```
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.storageAdmin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.storageAdmin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.storageAdmin
```

To enable Jenkins to create/delete Redis Memorystores:

```
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/redis.admin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/redis.admin
```

To enable Jenkins to resize GKE clusters and to start/stop GCE instances:
```
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role projects/sandbox-narrative/roles/powerbutton
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role projects/staging-narrative/roles/powerbutton
```

To enable Jenkins to update terraform.tfstate in informationtechnology buckets.
```
gcloud projects add-iam-policy-binding informationtechnology --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.objectAdmin
```

To enable Jenkins to create/destroy compute instances in neo-narrative:
```
gcloud projects add-iam-policy-binding neo-narrative --member serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.instanceAdmin
```
Add the Jenkins user as a storage object admin for the `narrative-devops` Google bucket.

Enable the GCE API and Cloud Resource API.

Create the jenkins-image in the jenkins-worker project. Install:

- Python3 and pip3
- Ansible
- Terraform
- Google Cloud SDK
- Docker and docker-compose

```
apt-get update && apt-get upgrade -y
apt-get install -y python3 python3-pip
apt-get install -y apt-transport-https ca-certificates curl software-properties-common jq vim unzip
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) test" # should be stable soooooon
apt-get update && apt-get install -y docker-ce
curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
chmod a+x /usr/local/bin/docker-compose
pip3 install ansible
pip3 install apache-libcloud
wget https://releases.hashicorp.com/terraform/0.11.7/terraform_0.11.7_linux_amd64.zip
unzip terraform_0.11.7_linux_amd64.zip -d /usr/bin/
export CLOUD_SDK_REPO="cloud-sdk-$(lsb_release -c -s)"
echo "deb http://packages.cloud.google.com/apt $CLOUD_SDK_REPO main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
apt-get update && apt-get install -y google-cloud-sdk
apt-get install -y kubectl
apt-get install openjdk-8-jre-headless
```

Go to images. Create the image, name it `jenkins-image-2018-07-10`, for example.

To create the Python2.7 image:

```
apt-get update && apt-get upgrade -y
apt-get install -y python python-pip
apt-get install -y apt-transport-https ca-certificates curl software-properties-common jq vim unzip
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) test" # should be stable soooooon
apt-get update && apt-get install -y docker-ce
curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
chmod a+x /usr/local/bin/docker-compose
pip install ansible
wget https://releases.hashicorp.com/terraform/0.11.7/terraform_0.11.7_linux_amd64.zip
unzip terraform_0.11.7_linux_amd64.zip -d /usr/bin/
export CLOUD_SDK_REPO="cloud-sdk-$(lsb_release -c -s)"
echo "deb http://packages.cloud.google.com/apt $CLOUD_SDK_REPO main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
apt-get update && apt-get install -y google-cloud-sdk
apt-get install -y kubectl
apt-get install openjdk-8-jre-headless
```

Go to images. Create the image, name it `jenkins-image-python2-2018-07-10`.

## Debugging Tricks

- Track Jenkins log - 
- GitHub Hook Log for the job - <https://jenkins.narrative.cloud/job/test/GitHubPollLog/>
- Added both test and test-workspace. test-workspace not triggering. test triggers but the node is missing.
- About to test the generic webhook. Issues with the Github hook. First trying a few tests thought.

## Slack Configuration

Followed this guidance: 

- Slack integration - <https://narrativenetwork.slack.com/services/BB1LS6HT3?added=1>
- secret info - <https://medium.com/foxguard-development/securing-slack-authentication-tokens-in-jenkins-b898873dc177>
- kind of old - <https://www.christopherrung.com/2017/05/04/slack-build-notifications/>

# Configuring Jenkins deployment service account

The following commands will activate the informationtechnology gcloud config (needs to exist on the local
system), it will create the Jenkins service account (in the Google Cloud project
"informationtechnology"), it will generate the service-account keys for that account, and finally it will
bind that to the project "dev-www-narrative-network", with the role of "Google App Engine Deployer"
(<https://cloud.google.com/appengine/docs/admin-api/access-control#roles>).


If the Jenkins user doesn't exist (note the project is the `informationtechnology` project):

```
gcloud config configurations activate informationtechnology
gcloud iam service-accounts create jenkins --display-name "Jenkins service account."
gcloud iam service-accounts keys create ~/.config/gcloud/jenkins.json --iam-account jenkins@informationtechnology-205813.iam.gserviceaccount.com

```

Save the `jenkins.json` file in 1Password.

Give the service account access to the cluster:

```
gcloud projects add-iam-policy-binding sandbox-narrative --role=roles/container.viewer --member=serviceAccount:jenkins@informationtechnology-205813.iam.gserviceaccount.com

```

Apply the cluster role to the Jenkins account:

```
kubectl config use-context gke_sandbox-narrative_us-east1_sandbox-cluster
kubectl create -f dev-role.yml
kubectl config use-context gke_staging-narrative_us-east1_staging-cluster
kubectl create -f staging-role.yml
kubectl config use-context gke_production-narrative_us-central1_production-cluster
kubectl create -f production-role.yml

```

# Deploy Key

Create the SSH key and save in 1Password (should already exist as 'jenkins ssh key - narrative-platform repository').

