resource "google_compute_instance" "neo-database-01" {
 project = "${ var.project  }"
 zone = "us-east1-b"
 name = "neo-database-01"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.neo-database-01-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 labels {
   environment = "neo"
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
   source = "neo-database-01-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["neo","prometheus-node-exporter"]
}

resource "google_compute_instance" "neo-01" {
 project = "${ var.project  }"
 zone = "us-east1-b"
 name = "neo-01"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.neo-01-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 labels {
   environment = "neo"
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
   source = "neo-01-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["neo","web","prometheus-node-exporter","haproxy-exporter"]
}

resource "google_compute_instance" "neo-02" {
 project = "${ var.project  }"
 zone = "us-west1-b"
 name = "neo-02"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.neo-02-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 labels {
   environment = "neo"
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
   source = "neo-02-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["neo","web","prometheus-node-exporter","haproxy-exporter"]
}

resource "google_compute_instance" "neo-03" {
 project = "${ var.project  }"
 zone = "europe-west2-b"
 name = "neo-03"
 machine_type = "${ var.type }"
 depends_on = ["google_compute_disk.neo-03-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 labels {
   environment = "neo"
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
   source = "neo-03-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["neo","web","prometheus-node-exporter","haproxy-exporter"]
}

resource "google_compute_instance" "neo-04" {
 project = "${ var.project  }"
 zone = "europe-west4-b"
 name = "neo-04"
 machine_type = "n1-standard-1"
 depends_on = ["google_compute_disk.neo-04-disk"]
 boot_disk {
   initialize_params {
     image = "${ var.image }"
   }
 }
 labels {
   environment = "neo"
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
   source = "neo-04-disk"
 }
 metadata {
   sshKeys = "${ var.sshkey }"
 }
 metadata_startup_script = <<SCRIPT
 # echo out the SSH key on first boot...
 /usr/bin/ssh-keygen -y -f /etc/ssh/ssh_host_ecdsa_key
 SCRIPT
 tags = ["neo","web","prometheus-node-exporter","haproxy-exporter"]
}

resource "google_compute_instance" "narrative-redemption-01" {
 project = "${ var.project }"
 zone = "us-east1-b"
 name = "narrative-redemption-01"
 machine_type = "${ var.type }"
 boot_disk {
   initialize_params {
     image = "${ var.image }"
     size  = "100"
   }
 }
 labels {
   environment = "neo"
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
 tags = ["redemption"]
}
