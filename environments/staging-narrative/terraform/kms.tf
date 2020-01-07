resource "google_kms_key_ring" "staging-key-ring" {
  project  = "staging-narrative"
  name     = "staging-key-ring"
  location = "us-east1"
}

resource "google_kms_crypto_key" "mysql-crypto-key" {
  name     = "mysql-crypto-key"
  key_ring = "${google_kms_key_ring.staging-key-ring.id}"
}

data "google_kms_secret" "sql-root-password" {
  crypto_key = "${google_kms_crypto_key.mysql-crypto-key.id}"
  ciphertext = "CiQAOvw3zTaNw9QHFrlsNViuD+mdeB+Jx4YVg3gF/iKX1WNoGokSUABhA/WKgEdbqPv4Z5uCKHLZ0MaB3KrzZyZigb5IW9aZfuBB4s0kQu6pDG1FNI22QRQT9q9Ihgiqz9ctNYtb5GE3xstdajkuaS2P22aR0am6"
}

