resource "google_storage_bucket" "narrative-backups" {
  project = "${ var.project  }"
  name     = "narrative-backups"
  location = "US"
  versioning = {
    enabled = "true"
  }

  # We explicitly prevent destruction using terraform. Remove this only if you really know what you're doing.
  lifecycle {
    prevent_destroy = true
  }
}

resource "google_storage_bucket_acl" "narrative-backups-acl" {
  bucket = "${google_storage_bucket.narrative-backups.name}"

  role_entity = [
    "OWNER:user-jenkins@informationtechnology-205813.iam.gserviceaccount.com",
    "OWNER:project-owners-751707143261",
    "OWNER:project-editors-751707143261",
    "READER:project-viewers-751707143261"
  ]
}
