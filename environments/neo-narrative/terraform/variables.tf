# Gcloud Settings

# Instance type to use 
variable "type" {
  type    = "string"
  default = "n1-standard-2"
}

variable "project" {
  type    = "string"
  default = "neo-narrative"
}

variable "region" {
  type    = "string"
  default = "us-east1"
}

variable "region_zone" {
  type    = "string"
  default = "us-east1-b"
}

variable "image" {
  type    = "string"
  default = "debian-9"
}

variable "sshkey" {
  type    = "string"
  default = "bot-ops:ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDM11v4W6l8PVBF0HMforK+WBlz9vc2NC0Qq+NWCA/eWGjcowhQa9HpLBtszL9YLS8j9n6TXQ4o1MznVU87TY9wSohymFXUnMgGnmaYtybe0M+6buIkzoVRZbPEXVTanv3Ujfm6NxjWo5PITW1PK7gszCVUSlQgsR6v5iM7mY4UoRGELcMDvFw5X5ZGsedfEpRC+gWvQIVJhxAjAkEWmrDiYcnD6d9xEh4GPC6XJNTCGGITpFmmcReMUe4VUD7ctCkiOAQ/QTdXdtjJIOcj/XU2X+mhyQj8LxzOzHRfJL872ZvMSX8ODyd+hP5Uwm/YFNARAf3SHmQUxIHgg3KDC5zF"
}

