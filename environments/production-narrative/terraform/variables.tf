# Gcloud Settings

# Instance type to use 
variable "type" {
  type    = "string"
  default = "n1-standard-1"
}

# Cloud SQL machine type
variable "sql_machine_type" {
  type    = "string"
  default = "db-n1-standard-2"
}

variable "project" {
  type    = "string"
  default = "production-narrative"
}

variable "region" {
  type    = "string"
  default = "us-central1"
}

variable "image" {
  type    = "string"
  default = "debian-9"
}

variable "db_version" {
  type    = "string"
  default = "MYSQL_5_7"
}

# The narrative bot-ops SSH key - PRODUCTION
variable "sshkey" {
  type    = "string"
  default = "bot-ops:ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCpf9oYjAHPBGP40e1tb94wjdzEnENsMiEDgwz2zro9qfNESL7vVGKRv2KkcFk+5maN1nwBxui7/T9uSEDR8LKfvEk7K+zyc/VnJ9ZEYst5asMn1IPsAv8L+a3Px/ed4RiFR/7rL67oK60UhWOc6Af3rtjMpawYqkWYY4bCf1oYHjaD9ZTcQ4s2O1sowh5C0WWYZH4O97sX+g28GOcbC3L5z0c93aS5GDM2QUcF/rqudLCUKSvScbq7eQ04kBG5lzAFynC18cMjg9+oyAgiHZ4gRYwji9oP08FeT7dUBwpzdSyBPE+cOW2cTDNB6/lUhRCW6cs39nec5f89BT6oKllr"
}

