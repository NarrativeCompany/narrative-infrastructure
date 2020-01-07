resource "google_kms_key_ring" "key-ring" {
  project  = "sandbox-narrative"
  name     = "key-ring"
  location = "us-east1"
}

resource "google_kms_crypto_key" "mysql-crypto-key" {
  name     = "mysql-crypto-key"
  key_ring = "${ google_kms_key_ring.key-ring.id }"
}

data "google_kms_secret" "sql-user-password" {
  crypto_key = "${ google_kms_crypto_key.mysql-crypto-key.id }"
  ciphertext = "CiQAqGBTKoOS2nLNSRHMu6CIqkqvTNhpR77iJbFTggCh6xgCr9gSQQCCU/K58cv7X94cQBGYutrfNatb63GmuGFnksjWFdXoGtCu76lczeMqQATaVbx1ijsYmyqznS5Cu0nNKVaCa+kv"
}

data "google_kms_secret" "sql-root-password" {
  crypto_key = "${ google_kms_crypto_key.mysql-crypto-key.id }"
  ciphertext = "CiQAqGBTKoOS2nLNSRHMu6CIqkqvTNhpR77iJbFTggCh6xgCr9gSQQCCU/K58cv7X94cQBGYutrfNatb63GmuGFnksjWFdXoGtCu76lczeMqQATaVbx1ijsYmyqznS5Cu0nNKVaCa+kv"
}
