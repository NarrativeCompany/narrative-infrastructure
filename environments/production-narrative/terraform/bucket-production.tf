resource "google_storage_bucket" "production-images" {
  name     = "images.narrative.org"
  location = "US"

  website {
    main_page_suffix = "index.html"
    not_found_page   = "404.html"
  }
}

resource "google_storage_bucket_object" "production-index" {
  name   = "index.html"
  bucket = "${google_storage_bucket.production-images.name}"
  source = "production-images/index.html"
}

resource "google_storage_bucket_object" "producton-404" {
  name   = "404.html"
  bucket = "${google_storage_bucket.production-images.name}"
  source = "production-images/404.html"
}

resource "google_storage_bucket_acl" "production-images-acl" {
  bucket = "${google_storage_bucket.production-images.name}"

  default_acl = "publicread"

  role_entity = [
    "OWNER:project-editors-946796390526",
    "OWNER:project-owners-356021690975",
    "OWNER:project-owners-946796390526",
    "READER:allUsers",
    "WRITER:user-production-images-upload@production-narrative.iam.gserviceaccount.com",
    "READER:project-viewers-946796390526"
  ]
}
