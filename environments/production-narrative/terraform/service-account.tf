resource "google_service_account" "production-images" {
  account_id   = "production-images-upload"
  display_name = "production-images-upload"
}
