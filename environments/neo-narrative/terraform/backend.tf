terraform {
 backend "gcs" {
   bucket  = "neo-narrative-terraform"
   path    = "terraform.tfstate"
   project = "informationtechnology-205813"
 }
}
