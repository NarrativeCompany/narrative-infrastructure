resource "google_service_account" "local-images" {
  account_id   = "local-images-upload"
  display_name = "local-images-upload"
}

resource "google_service_account" "dev-images" {
  account_id   = "dev-images-upload"
  display_name = "dev-images-upload"
}
