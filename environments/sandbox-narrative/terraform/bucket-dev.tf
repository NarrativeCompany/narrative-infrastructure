resource "google_storage_bucket" "dev-images" {
  name     = "dev-images.narrative.org"
  location = "US"

  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}

resource "google_storage_bucket_object" "dev-index" {
  name   = "index.html"
  bucket = "${google_storage_bucket.dev-images.name}"
  source = "dev-images/index.html"
}

resource "google_storage_bucket_object" "dev-404" {
  name   = "404.html"
  bucket = "${google_storage_bucket.dev-images.name}"
  source = "dev-images/404.html"
}

resource "google_storage_bucket_acl" "dev-images-acl" {
  bucket = "${google_storage_bucket.dev-images.name}"

  default_acl = "publicread"

  role_entity = [
    "OWNER:project-owners-946796390526",
    "OWNER:project-editors-946796390526",
    "READER:project-viewers-946796390526",
    "WRITER:user-dev-images-upload@sandbox-narrative.iam.gserviceaccount.com",
    "READER:allUsers"
  ]
}
