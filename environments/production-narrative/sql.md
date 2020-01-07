# Deploy Google Cloud SQL to the production

Deploy a MySQL cluster to the production-narrative project


# Create the root passwords:

Create an entry titled "Google Cloud SQL production-master root user" in 1password narrative -> operations vault.  


# Enable the Google Cloud KMS API:

https://console.developers.google.com/apis/api/cloudkms.googleapis.com/overview?project=356021690975

Click Enable API


# Create the cryptographic keys:

Create the [kms.tf](terraform/kms.tf) file that will generate the key ring and crypto key


# Terraform the keyring and crypto key

Plan it to kms.plan and apply it

```
terraform plan -target=google_kms_key_ring.key-ring -target=google_kms_crypto_key.mysql-crypto-key -out=kms.plan
terraform apply kms.plan
```


# Encrypt the plaintext password for the root user on Google KMS:

```
echo -n the-plaintext-password-for-root-create-in-1password-goes-here | gcloud kms encrypt --project production-narrative --location us-central1 --keyring key-ring --key mysql-crypto-key --plaintext-file - --ciphertext-file - | base64
```


# Save the encrypted password for the root user:

Copy the resulting blob from above to a new data section of the [kms.tf](terraform/kms.tf) file.

```
data "google_kms_secret" "sql-root-password" {
  crypto_key = "${google_kms_crypto_key.mysql-crypto-key.id}"
  ciphertext = "insert-blob-here"
}
```


# Define the global variables:

Create the [variables.tf](terraform/variables.tf) information required to create the instance.


# Create the terraform file to create the instance:

The [sql.tf](terraform/sql.tf) file will create the instance.


# Terraform the instance:

Plan it to sql.plan and apply it

```
terraform plan -target=google_sql_database.sql-database -target=google_sql_user.root -target=google_sql_database_instance.production-failover -target=google_sql_database_instance.production-master -out=sql.plan
terraform apply sql.plan
```
Remove the `sql.plan` file when finished.

# Note:

Do not use the GUI to create the key ring and the crypto key as terraform will not recognize it.
