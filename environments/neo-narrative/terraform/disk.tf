resource "google_compute_disk" "neo-database-01-disk" {
  name  = "neo-database-01-disk"
  type  = "pd-ssd"
  zone  = "us-east1-b"
  size  = "100"
}

resource "google_compute_disk" "neo-01-disk" {
  name  = "neo-01-disk"
  type  = "pd-ssd"
  zone  = "us-east1-b"
  size  = "30"
}

resource "google_compute_disk" "neo-02-disk" {
  name  = "neo-02-disk"
  type  = "pd-ssd"
  zone  = "us-west1-b"
  size  = "30"
}

resource "google_compute_disk" "neo-03-disk" {
  name  = "neo-03-disk"
  type  = "pd-ssd"
  zone  = "europe-west2-b"
  size  = "30"
}

resource "google_compute_disk" "neo-04-disk" {
  name  = "neo-04-disk"
  type  = "pd-ssd"
  zone  = "europe-west4-b"
  size  = "30"
}
