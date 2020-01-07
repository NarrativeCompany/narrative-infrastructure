terraform {
 backend "gcs" {
   bucket  = "narrative-devops"
   prefix    = "terraform/staging-narrative/terraform.tfstate"
   project = "informationtechnology-205813"
 }
}
