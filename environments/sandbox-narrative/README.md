# sandbox-narrative

This is the `sandbox-narrative` environment. It is a [project in Google Cloud](https://console.cloud.google.com/home/dashboard?project=sandbox-narrative&organizationId=149049775531).

Quickstart:

- From 1Password, copy the `Google Cloud it-terraform credentials` file to `~/.config/gcloud/it-terraform.json`.
- Export these Terraform settings:

```
export GOOGLE_APPLICATION_CREDENTIALS=~/.config/gcloud/it-terraform.json
export GOOGLE_PROJECT=sandbox-narrative
```

- Initialize:

```
cd terraform
terraform init
```

# Requirements

- The Google Cloud Project `sandbox-narrative`
- Billing enabled for the `sandbox-narrative` project.
- Various APIs enabled (you can do this by visiting the Google Cloud UI).
- Enable the IAM API - <https://console.developers.google.com/apis/api/iam.googleapis.com/overview?project=751707143261>

# Terraform Setup

This will use the `it-terraform@informationtechnology-205813.iam.gserviceaccount.com` service account. It should have already been created (instructions TBD).

To add the sandbox-narrative project:

Activate the project (see below for instructions to add it):

```
gcloud config configurations activate sandbox-narrative
```

Configure the `it-terraform` access:

```
# allow access to the sandbox-narrative project to create/deploy
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/viewer
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.instanceAdmin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.networkAdmin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.securityAdmin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/redis.admin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudsql.admin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudkms.admin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/cloudkms.cryptoKeyEncrypterDecrypter
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.admin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/iam.serviceAccountAdmin
gcloud projects add-iam-policy-binding sandbox-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/iam.serviceAccountUser
# allow gcloud bucket access
gcloud projects add-iam-policy-binding informationtechnology-205813 --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.admin
```

Add the `it-terraform` user as a verified owner - <https://www.google.com/webmasters/verification/details?hl=en&domain=narrative.org>.

Configure the Terraform Remote State:


Create hash for mysql user passwords with Google KMS:
```
echo -n your-password-goes-here | gcloud kms encrypt \
--project sandbox-narrative \
--location us-east1 \
--keyring key-ring \
--key mysql-crypto-key \
--plaintext-file - \
--ciphertext-file - \
| base64
```
