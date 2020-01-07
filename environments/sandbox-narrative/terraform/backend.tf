terraform {
 backend "gcs" {
   bucket  = "narrative-devops"
   prefix    = "terraform/sandbox-narrative/terraform.tfstate"
   project = "informationtechnology-205813"
 }
}
