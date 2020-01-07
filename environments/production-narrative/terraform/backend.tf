terraform {
 backend "gcs" {
   bucket  = "narrative-devops"
   prefix    = "terraform/production-narrative/terraform.tfstate"
   project = "informationtechnology-205813"
 }
}
