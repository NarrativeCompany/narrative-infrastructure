resource "google_compute_instance" "solr-01" {
 project = "${ var.project  }"
 zone = "us-east1-b"
 name = "solr-01"
 allow_stopping_for_update = "true"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.solr-01-disk"]
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
   source = "solr-01-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["staging","solr","prometheus-node-exporter","solr-exporter"]
  service_account {
    email = "google-fluentd@staging-narrative.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }
}

resource "google_compute_instance" "solr-02" {
 project = "${ var.project  }"
 zone = "us-east1-c"
 name = "solr-02"
 allow_stopping_for_update = "true"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.solr-02-disk"]
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
   source = "solr-02-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["staging","solr","prometheus-node-exporter","solr-exporter"]
  service_account {
    email = "google-fluentd@staging-narrative.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }
}

resource "google_compute_instance" "solr-03" {
 project = "${ var.project  }"
 zone = "us-east1-d"
 name = "solr-03"
 allow_stopping_for_update = "true"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.solr-03-disk"]
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
   source = "solr-03-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["staging","solr","prometheus-node-exporter","solr-exporter"]
  service_account {
    email = "google-fluentd@staging-narrative.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }
}
