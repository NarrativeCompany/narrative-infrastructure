resource "google_compute_firewall" "allow-all-web" {
   name = "allow-all-web"
   network = "default"

  allow {
    protocol = "tcp"
    ports    = ["80", "443"]
  }

  target_tags = ["web"]
}

resource "google_compute_firewall" "allow-prometheus" {
   name = "allow-prometheus"
   network = "default"

  allow {
    protocol = "tcp"
    ports    = ["9143"]
  }

  target_tags = ["prometheus-node-exporter"]
}

resource "google_compute_firewall" "allow-haproxy-exporter" {
   name = "allow-haproxy-exporter"
   network = "default"

  allow {
    protocol = "tcp"
    ports    = ["9144"]
  }

  target_tags = ["haproxy-exporter"]
}
