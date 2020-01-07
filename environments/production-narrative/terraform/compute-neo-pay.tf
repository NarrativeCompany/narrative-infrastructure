resource "google_compute_instance" "neo-pay-01" {
 project = "${ var.project  }"
 zone = "us-central1-a"
 name = "neo-pay-01"
 allow_stopping_for_update = "true"
 machine_type = "n1-standard-2"
 depends_on = ["google_compute_disk.neo-pay-01-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 lifecycle {
   ignore_changes = ["metadata.sshKeys"]
 }
 network_interface {
   network = "default"
   access_config {
   }
 }
 attached_disk {
   source = "neo-pay-01-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["production","neo-pay","prometheus-node-exporter"]
 service_account {
   email = "google-fluentd@production-narrative.iam.gserviceaccount.com"
   scopes = ["https://www.googleapis.com/auth/cloud-platform"]
 }
}
resource "google_compute_instance" "neo-pay-02" {
 project = "${ var.project  }"
 zone = "us-central1-b"
 name = "neo-pay-02"
 allow_stopping_for_update = "true"
 machine_type = "n1-standard-2"
 depends_on = ["google_compute_disk.neo-pay-02-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 lifecycle {
   ignore_changes = ["metadata.sshKeys"]
 }
 network_interface {
   network = "default"
   access_config {
   }
 }
 attached_disk {
   source = "neo-pay-02-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["production","neo-pay","prometheus-node-exporter"]
 service_account {
   email = "google-fluentd@production-narrative.iam.gserviceaccount.com"
   scopes = ["https://www.googleapis.com/auth/cloud-platform"]
 }
}
resource "google_compute_instance" "neo-pay-03" {
 project = "${ var.project  }"
 zone = "us-central1-c"
 name = "neo-pay-03"
 allow_stopping_for_update = "true"
 machine_type = "n1-standard-2"
 depends_on = ["google_compute_disk.neo-pay-03-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 lifecycle {
   ignore_changes = ["metadata.sshKeys"]
 }
 network_interface {
   network = "default"
   access_config {
   }
 }
 attached_disk {
   source = "neo-pay-03-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["production","neo-pay","prometheus-node-exporter"]
 # tags = ["production","neo-pay"]
 service_account {
   email = "google-fluentd@production-narrative.iam.gserviceaccount.com"
   scopes = ["https://www.googleapis.com/auth/cloud-platform"]
 }
}
