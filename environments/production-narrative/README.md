# production-narrative

This is the `production-narrative` environment. It is a [project in Google Cloud](https://console.cloud.google.com/home/dashboard?project=production-narrative&organizationId=149049775531).

# Creating the Project and Initializing 

To create the production-narrative project:

```
gcloud projects create production-narrative
```

Enable billing. You can do this by going to the compute instances (<https://console.cloud.google.com/compute/instances?project=production-narrative>), and following the GUI instructions (CLI commands not captured).

To add the production-narrative project:

Activate the project (if you have not configured it, run `gcloud init` and follow the instructions):

```
gcloud config configurations activate production-narrative
```

# Requirements

- The Google Cloud Project `production-narrative`
- Billing enabled for the `production-narrative` project.
- The API enabled for Compute, Memorystore, SQL, etc. This can be done by visiting the Google Cloud web interface (example, just visiting the [production-narrative Memorystore](https://console.cloud.google.com/memorystore/redis/instances?project=production-narrative&organizationId=149049775531) will enable the API).

# Terraform Setup

This will use the `it-terraform@informationtechnology-205813.iam.gserviceaccount.com` service account. It should have already been created (instructions TBD).

Configure the `it-terraform` access:

```
# allow access to the production-narrative project to create/deploy
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/viewer
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.instanceAdmin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.networkAdmin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.securityAdmin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/redis.admin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudsql.admin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudkms.admin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudkms.cryptoKeyEncrypterDecrypter
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.admin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/iam.serviceAccountAdmin
gcloud projects add-iam-policy-binding production-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/iam.serviceAccountUser
# allow gcloud bucket access
gcloud projects add-iam-policy-binding informationtechnology-205813 --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.admin
```

The Terraform remote state is viewable in the `narrative-devops` bucket, in the `terraform` directory - <https://console.cloud.google.com/storage/browser/narrative-devops/terraform/?project=informationtechnology-205813&organizationId=149049775531>

# Local Terraform Config

- From 1Password, copy the `Google Cloud it-terraform credentials` file to `~/.config/gcloud/it-terraform.json`.
- Export these Terraform settings:

```
export GOOGLE_APPLICATION_CREDENTIALS=~/.config/gcloud/it-terraform.json
export GOOGLE_PROJECT=production-narrative
```

- Initialize:

```
cd terraform
terraform init
```

See the `README.md` in the `terraform` directory.

# Additional Google Bucket Configuration

The index and 404 are being created without public access. An easy way to fix that is to delete them, and re-Terraform. Go to <https://console.cloud.google.com/storage/browser/images.narrative.org?project=production-narrative&organizationId=149049775531>. Delete `404.html` and `index.html`.

Rerun:

```
terraform apply -target google_storage_bucket_object.production-index -target google_storage_bucket_object.producton-404
```
