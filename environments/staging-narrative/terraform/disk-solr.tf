resource "google_compute_disk" "solr-01-disk" {
  project = "${ var.project  }"
  name  = "solr-01-disk"
  type  = "pd-ssd"
  zone  = "us-east1-b"
  size  = "30"
}

resource "google_compute_disk" "solr-02-disk" {
  project = "${ var.project  }"
  name  = "solr-02-disk"
  type  = "pd-ssd"
  zone  = "us-east1-c"
  size  = "30"
}

resource "google_compute_disk" "solr-03-disk" {
  project = "${ var.project  }"
  name  = "solr-03-disk"
  type  = "pd-ssd"
  zone  = "us-east1-d"
  size  = "30"
}

