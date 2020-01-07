resource "google_compute_disk" "solr-01-disk" {
  name  = "solr-01-disk"
  type  = "pd-ssd"
  zone  = "us-central1-a"
  size  = "30"
}

resource "google_compute_disk" "solr-02-disk" {
  name  = "solr-02-disk"
  type  = "pd-ssd"
  zone  = "us-central1-b"
  size  = "30"
}

resource "google_compute_disk" "solr-03-disk" {
  name  = "solr-03-disk"
  type  = "pd-ssd"
  zone  = "us-central1-c"
  size  = "30"
}

