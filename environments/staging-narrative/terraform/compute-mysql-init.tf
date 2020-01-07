resource "google_compute_instance" "mysql-01" {
  project = "${ var.project  }"
  zone = "us-east1-b"
  name = "mysql-01"
  allow_stopping_for_update = "true"
  machine_type = "${ var.type }"
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
  metadata {
    sshKeys = "${ var.sshkey }"
  }
  metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
  tags = ["staging","mysql"]
  service_account {
    email = "staging-sql@staging-narrative.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }
}
