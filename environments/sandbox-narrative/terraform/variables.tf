# Gcloud Settings

# Instance type to use 
variable "type" {
  type    = "string"
  default = "g1-small"
}

# Cloud SQL machine type
variable "sql_machine_type" {
  type    = "string"
  default = "db-g1-small"
}

variable "project" {
  type    = "string"
  default = "sandbox-narrative"
}

variable "region" {
  type    = "string"
  default = "us-east1"
}

variable "image" {
  type    = "string"
  default = "debian-9"
}

variable "db_version" {
  type    = "string"
  default = "MYSQL_5_7"
}

variable "sshkey" {
  type    = "string"
  default = "bot-ops:ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDM11v4W6l8PVBF0HMforK+WBlz9vc2NC0Qq+NWCA/eWGjcowhQa9HpLBtszL9YLS8j9n6TXQ4o1MznVU87TY9wSohymFXUnMgGnmaYtybe0M+6buIkzoVRZbPEXVTanv3Ujfm6NxjWo5PITW1PK7gszCVUSlQgsR6v5iM7mY4UoRGELcMDvFw5X5ZGsedfEpRC+gWvQIVJhxAjAkEWmrDiYcnD6d9xEh4GPC6XJNTCGGITpFmmcReMUe4VUD7ctCkiOAQ/QTdXdtjJIOcj/XU2X+mhyQj8LxzOzHRfJL872ZvMSX8ODyd+hP5Uwm/YFNARAf3SHmQUxIHgg3KDC5zF"
}

