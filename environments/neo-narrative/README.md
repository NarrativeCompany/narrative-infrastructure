# neo

This is the `neo-narrative` environment. It is a [project in Google Cloud](https://console.cloud.google.com/home/dashboard?project=neo-narrative&organizationId=149049775531).

Quickstart:

- From 1Password, copy the `Google Cloud it-terraform credentials` file to `~/.config/gcloud/it-terraform.json`.
- Export these Terraform settings:

```
export GOOGLE_APPLICATION_CREDENTIALS=~/.config/gcloud/it-terraform.json
export GOOGLE_PROJECT=neo-narrative
```

- Initialize:

```
cd terraform
terraform init
```

# Requirements

- The Google Cloud Project `neo-narrative`
- Billing enabled for the `neo-narrative` project.

# Terraform Setup

This will use the `it-terraform@informationtechnology-205813.iam.gserviceaccount.com` service account. It should have already been created (instructions TBD).

To add the neo-narrative project:

Activate the project (see below for instructions to add it):

```
gcloud config configurations activate neo-narrative
```

Configure the `it-terraform` access:

```
# allow access to the neo-narrative project to create/deploy
gcloud projects add-iam-policy-binding neo-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/viewer
gcloud projects add-iam-policy-binding neo-narrative --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/compute.instanceAdmin
# allow gcloud bucket access
gcloud projects add-iam-policy-binding informationtechnology-205813 --member serviceAccount:it-terraform@informationtechnology-205813.iam.gserviceaccount.com --role roles/storage.admin
```

Configure the Terraform Remote State:

```
gsutil mb -p informationtechnology-205813 gs://neo-narrative-terraform
gsutil versioning set on gs://neo-narrative-terraform
```

The bucket is now viewable at <https://console.cloud.google.com/storage/browser/neo-narrative-terraform?project=informationtechnology-205813>.


# To Add the neo-narrative Project

To add the project:

```
gcloud config configurations create neo-narrative
gcloud init
```

Select option 1.

Select your Narrative email address.

Select the `neo-narrative` project.

Select `n` (no need to set a default region/zone).

From 1Password, copy the `Google Cloud it-terraform credentials` file to `~/.config/gcloud/it-terraform.json`.

# Links

- https://cloud.google.com/community/tutorials/managing-gcp-projects-with-terraform
- https://cloud.google.com/iam/docs/understanding-service-accounts#managing_service_account_keys
- https://medium.com/@josephbleroy/using-terraform-with-google-cloud-platform-part-1-n-6b5e4074c059
