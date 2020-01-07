resource "google_redis_instance" "redis" {
  project = "${ var.project }"
  name = "redis-01"
  tier = "STANDARD_HA"
  memory_size_gb = 1
  region = "${ var.region }"
  authorized_network = "projects/${ var.project }/global/networks/default"
  alternative_location_id = "us-east1-b"
  redis_version = "REDIS_3_2"
  display_name = "redis-01"
}
