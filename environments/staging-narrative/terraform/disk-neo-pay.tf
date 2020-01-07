resource "google_compute_disk" "neo-pay-01-disk" {
  name  = "neo-pay-01-disk"
  type  = "pd-ssd"
  zone  = "us-east1-b"
  size  = "30"
}
resource "google_compute_disk" "neo-pay-02-disk" {
  name  = "neo-pay-02-disk"
  type  = "pd-ssd"
  zone  = "us-east1-c"
  size  = "30"
}
