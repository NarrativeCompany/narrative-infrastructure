resource "google_storage_bucket" "local-images" {
  name     = "local-images.narrative.org"
  location = "US"

  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}

resource "google_storage_bucket_object" "local-index" {
  name   = "index.html"
  bucket = "${google_storage_bucket.local-images.name}"
  source = "local-images/index.html"
}

resource "google_storage_bucket_object" "local-404" {
  name   = "404.html"
  bucket = "${google_storage_bucket.local-images.name}"
  source = "local-images/404.html"
}

resource "google_storage_bucket_acl" "local-images-acl" {
  bucket = "${google_storage_bucket.local-images.name}"

  default_acl = "publicread"

  role_entity = [
    "OWNER:project-owners-946796390526",
    "OWNER:project-editors-946796390526",
    "READER:project-viewers-946796390526",
    "WRITER:user-local-images-upload@sandbox-narrative.iam.gserviceaccount.com",
    "READER:allUsers"
  ]
}
