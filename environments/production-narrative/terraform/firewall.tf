resource "google_compute_firewall" "allow-solr-internal" {
   name = "allow-solr-internal"
   network = "default"

  allow {
    protocol = "tcp"
    ports    = ["2181","8983"]
  }

  source_ranges = ["10.0.0.0/8"]
  target_tags = ["solr"]
}

resource "google_compute_firewall" "allow-prometheus" {
   name = "allow-prometheus"
   network = "default"

  allow {
    protocol = "tcp"
    ports    = ["9143"]
  }

  source_ranges = ["10.0.0.0/8"]
  target_tags = ["prometheus-node-exporter"]
}
resource "google_compute_firewall" "allow-solr-exporter" {
   name = "allow-solr-exporter"
   network = "default"

  allow {
    protocol = "tcp"
    ports    = ["9146"]
  }

  source_ranges = ["10.0.0.0/8"]
  target_tags = ["solr-exporter"]
}
