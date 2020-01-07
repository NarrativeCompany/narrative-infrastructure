resource "google_kms_key_ring" "key-ring" {
  project  = "production-narrative"
  name     = "key-ring"
  location = "us-central1"
}

resource "google_kms_crypto_key" "mysql-crypto-key" {
  name     = "mysql-crypto-key"
  key_ring = "${google_kms_key_ring.key-ring.id}"
}

data "google_kms_secret" "sql-root-password" {
  crypto_key = "${google_kms_crypto_key.mysql-crypto-key.id}"
  ciphertext = "CiQA+jxoZ1LpMG0mYhQNrz+oIL+Tok38lkxf9hwQyHIvgm9ZRwsSVABS3R+gnv/F0AozfI1JXrTPN7lzvaJqfj8ZwL23tl18111MteyQMOfOMtndbI6KAWTDVa3oXMvEjz9Mjsyon4J+nNA7QdCOE4PlaJ6m7oum/GWwRg=="
}

