terraform {
 backend "gcs" {
   bucket  = "narrative-devops"
   prefix    = "terraform/informationtechnology-205813/terraform.tfstate"
   project = "informationtechnology-205813"
 }
}
