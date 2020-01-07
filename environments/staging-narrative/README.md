# staging-narrative

This is the `staging-narrative` environment. It is a [project in Google Cloud](https://console.cloud.google.com/home/dashboard?project=staging-narrative&organizationId=149049775531).

# Creating the Project and Initializing 

To create the staging-narrative project:

```
gcloud projects create staging-narrative
```

Enable billing. You can do this by going to the compute instances (<https://console.cloud.google.com/compute/instances?project=staging-narrative>), and following the GUI instructions (CLI commands not captured).

To add the staging-narrative project:

Activate the project (if you have not configured it, run `gcloud init` and follow the instructions):

```
gcloud init
```

We created configuration for the stating-narrative project (not default)
We set the default zone to us-east1-c

```
gcloud config configurations activate staging-narrative
```

# Requirements

- The Google Cloud Project `staging-narrative`
- Billing enabled for the `staging-narrative` project.

# Terraform Setup

This will use the `it-terraform@informationtechnology-205813.iam.gserviceaccount.com` service account. It should have already been created (instructions TBD).

Configure the `it-terraform` access:

```
# allow access to the staging-narrative project to create/deploy
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/viewer
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.instanceAdmin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.networkAdmin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.securityAdmin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/redis.admin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudsql.admin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudkms.admin
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudkms.cryptoKeyEncrypterDecrypter
gcloud projects add-iam-policy-binding staging-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/iam.serviceAccountUser
# allow gcloud bucket access
gcloud projects add-iam-policy-binding informationtechnology-205813 --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.admin
```

The Terraform remote state is viewable in the `narrative-devops` bucket, in the `terraform` directory - <https://console.cloud.google.com/storage/browser/narrative-devops/terraform/?project=informationtechnology-205813&organizationId=149049775531>

# Local Terraform Config

- From 1Password, copy the `Google Cloud it-terraform credentials` file to `~/.config/gcloud/it-terraform.json`.
- Export these Terraform settings:

```
export GOOGLE_APPLICATION_CREDENTIALS=~/.config/gcloud/it-terraform.json
export GOOGLE_PROJECT=staging-narrative
```

- Initialize:

```
cd infrastructure/environments/
mkdir -p staging-narrative/terraform
cd staging-narrative/terraform
cat <<EOF >backend.tf
terraform {
 backend "gcs" {
   bucket  = "narrative-devops"
   prefix    = "terraform/staging-narrative/terraform.tfstate"
   project = "informationtechnology-205813"
 }
}
EOF
terraform init
```
