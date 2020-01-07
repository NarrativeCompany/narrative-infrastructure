resource "google_compute_disk" "neo-pay-01-disk" {
  name  = "neo-pay-01-disk"
  type  = "pd-ssd"
  zone  = "us-central1-a"
  size  = "30"
}
resource "google_compute_disk" "neo-pay-02-disk" {
  name  = "neo-pay-02-disk"
  type  = "pd-ssd"
  zone  = "us-central1-b"
  size  = "30"
}
resource "google_compute_disk" "neo-pay-03-disk" {
  name  = "neo-pay-03-disk"
  type  = "pd-ssd"
  zone  = "us-central1-c"
  size  = "100"
}
