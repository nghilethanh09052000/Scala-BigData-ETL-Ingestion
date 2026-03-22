package com.grab.constants

object StringConstants {
  val S3PREFIX = "s3://"

  object VaultConstants {
    //Secret keys
    val HIVE_METASTORE_SERVER_VAULT_KEY = "hive_metastore_server"
    val HIVE_METASTORE_DB_VAULT_KEY = "hive_metastore_db_name"
    val HIVE_METASTORE_USER_VAULT_KEY = "hive_metastore_user"
    val HIVE_METASTORE_PASSWORD_VAULT_KEY = "hive_metastore_password"

    val AZURE_HIVE_METASTORE_SERVER_VAULT_KEY = "azure_hive_metastore_server"
    val AZURE_HIVE_METASTORE_DB_VAULT_KEY = "azure_hive_metastore_db_name"
    val AZURE_HIVE_METASTORE_USER_VAULT_KEY = "azure_hive_metastore_user"
    val AZURE_HIVE_METASTORE_PASSWORD_VAULT_KEY = "azure_hive_metastore_password"
    val AZURE_HIVE_METASTORE_CONNECTION_SSL_FLAG = "azure_hive_metasotre_connection_ssl_flag"
  }
}
