package com.grab.mercury.constants

object Constants {

  // Input Constants
  val START_TIME_ARG: String = "start-time"
  val END_TIME_ARG: String = "end-time"
  val JOB_NAME_ARG: String = "job-name"
  val Input_TASK_ID_ARG: String = "task-id"
  val SCHEMA_NAME_ARG: String = "schema-name"
  val SCHEMA_LOCATION_ARG: String = "schema-location"
  val SOURCE: String = "source"
  val TRIGGER_BY_ARG = "trigger-by"

  object TRIGGER_BY extends Enumeration {
    type TRIGGER_BY = Value
    val Regular: Value = Value("regular")
    val DDL: Value = Value("hugo_tasks_router")
    val Backfill: Value = Value("backfill")

  }

  object ENV extends Enumeration {
    type ENV = Value
    val Prod: Value = Value("prod")
    val Staging: Value = Value("staging")
    val Local: Value = Value("local")
  }

  object RUNTIME extends Enumeration {
    type RUNTIME = Value
    val EMR: Value = Value("emr")
    val K8S: Value = Value("k8s")
  }

  val OnlineSource = "online"
  val InputTimeFormat: String = "yyyy-MM-dd:HH:mm:ss"
  val OracleInputTimeFormat: String = "yyyy-MM-dd HH:mm:ss"

  // End Input Constants

  // Catalog driver Input
  val Operation: String = "operation"

  object CatalogOperationType extends Enumeration {
    type CatalogOperationType = Value

    val CreateTable: Value = Value("create-table")
    val CreateSchema: Value = Value("create-schema")
    val DropTable: Value = Value("drop-table")
    val RecreateTable: Value = Value("recreate-table")
    val RepairHivePartitions: Value = Value("repair-hive-partitions")
  }

  object TableFormatType extends Enumeration {
    type TableFormatType = Value
    val Hive: Value = Value("hive")
    val Iceberg: Value = Value("iceberg")
  }

  // Vault Constants
  case class VaultEnvConf(server: String, port: String, secretsPath: String)

  case class VaultRuntimeConf(role: String, authPath: String)

  val VaultEnvConfMap: Map[ENV.ENV, VaultEnvConf] = Map(
    ENV.Staging -> VaultEnvConf("managed-vault-01.vault-v2-oss.stg-myteksi.com", "443", "v1/dt/data/default_ec2/mercury"),
    ENV.Prod -> VaultEnvConf("managed-vault-01.vault-v2-oss.myteksi.net", "443", "v1/dt/data/default_ec2/mercury")
  )


  /**
   * RUNTIME will still be kept for the use case of extension of adhoc operations later
   * We can use runtime to differentiate the different running environment
   */
  val VaultRuntimeConfMap: Map[ENV.ENV, VaultRuntimeConf] = Map(
    ENV.Staging -> VaultRuntimeConf("dt-staging-hugo-role", "v1/auth/aws/login"),
    ENV.Prod ->VaultRuntimeConf("dt-production-hugo-role", "v1/auth/aws/login")
  )

  val HashSalt: String = "hash_salt"
  val HashIterations: String = "hash_iterations"
  val MercuryUrlProd: String = "mercury_url_prod"
  val MercuryPortProd: String = "mercury_port_prod"
  val MercuryDbNameProd: String = "mercury_db_name_prod"
  val MercuryUserProd: String = "mercury_user_prod"
  val MercuryPassProd: String = "mercury_pass_prod"
  val LoggingPath: String = "logging_path"

  // End Vault Constants
  // SQL Engine Config
  val LEFT_ESCAPE_CHARACTER :String = "left_escape_character"
  val RIGHT_ESCAPE_CHARACTER :String = "right_escape_character"
  val DEFAULT_ESCAPE_CHARACTER : String =""
  // Loader config Constants
  //replica info
  val ReplicaId: String = "replica_id"
  // job info
  val JobIsHealthy: String = "is_healthy"
  // task info
  val ConsumerType: String = "consumer_type"
  val CommitterType: String = "committer_type"
  val DataTypeMapping: String = "datatype_mapping"
  val OrderColumn: String = "order_column"
  val PartitionKeySecondary: String = "partition_key_secondary"
  val CommitStrategy: String = "commit_strategy"
  val ConsumerStrategy: String = "consumer_strategy"
  val SchemaConverterClass: String = "schema_converter_class"
  val S3Bucket: String = "s3_bucket"
  val FsAuthority: String = "fs_authority"
  val OutputDirectory: String = "output_directory"
  val PartitionColumn: String = "partition_column"
  val JobGranularity: String = "job_granularity"
  val NumOfQueryPartitions: String = "num_of_query_partitions"
  val TaskMetadata: String = "task_metadata"
  val TaskId: String = "id"
  val UpdateUptoInDays: String = "update_upto_in_days"
  val NumOfFilesPerPartition: String = "num_of_files_per_partition"
  val QueryStep: String = "query_step"
  val FsScheme: String = "fs_scheme"
  val ForBackFill: String = "for_backfill"

  // task metadata parameters
  val IsMinMaxBoundCheckSkipped: String = "is_min_max_bound_check_skipped"
  val IsDataVersioned: String = "is_data_versioned"
  val VersionRetention: String = "version_retention"
  val DataOutputFormat: String = "data_output_format"
  val MaxLineNumPerFile: String = "max_line_num_per_file"
  val ThreadNumForMoveFileToTransformed: String = "thread_num_for_move_file_to_transformed"
  val RegisterPartitionsOnline: String = "register_partitions_online"
  val HivePartitionsConcurrency: String = "hive_partitions_concurrency"
  val WaitForFinalConsistencySeconds = "wait_for_final_consistency_seconds"
  val CheckReplicaLag: String = "check_replica_lag"
  val IdOffset: String = "id_offset"
  val IncStep: String = "inc_step"
  val LookAhead: String = "look_ahead"
  val LookBack: String = "look_back"
  val IdLookBack: String = "id_look_back"
  val Query: String = "query"
  val ExportFormat: String = "exportFormat"
  val TsColumn: String = "tsColumn"
  val DefaultHivePartitionsConcurrency: Int = 100
  val DefaultHiveReadMiniBatchSize: Int = 20000
  val DefaultThreadNumForMoveFileToTransformed: Int = 10
  val DefaultMaxLineNumPerFile: Int = 1000000
  val DefaultVersionsToRetain: Int = 2
  val DefaultWaitForFinalConsistencySeconds: Int = 10
  val NumOfRetryForMoveFileToTransformed: Int = 6
  val IsDataCountCheckEnabled: String = "is_data_count_check_enabled"
  val TableFormat: String = "table_type"

  // extended config
  val HiveTableName: String = "hive_table_name"
  val HiveSchema = "hive_db_name"
  val HiveDdl = "hive_ddl"
  val HiveStorageLocation = "hive_storage_location"

  val SourceServerEndpoint: String = "db_endpoint"
  val SourceDatabase: String = "db_name"
  val SourceTableName: String = "table_name"
  val SourceDbUserName: String = "db_user"
  val SourceDBPort: String = "db_port"
  val SourceDBType: String = "db_type"
  val SourceExtraConf: String = "db_extra_config"
  val SourceHash: String = "hash"
  val SourceType: String = "source_type"

  // binlog_compaction_task_info config
  val BinlogLookBackMinutes = "lookback_minutes"
  val BinlogLookAheadMinutes = "lookahead_minutes"
  val BinlogCompactionTaskMetadata: String = "binlog_compaction_task_metadata"
  val BinlogInputPathPrefix: String = "input_path" // eg. 's3://bucket/prefix/'
  val BinlogIsEnabled: String = "is_enabled"
  val BinlogInputFormat: String = "input_format"
  val BinlogDeleteApply: String = "cdc_delete_apply"
  val SqlLoaderTaskInfoId = "sql_loader_task_info_id"

  val ConfigDbName: String = "commons.config_db_name"
  val ConfigDbUrl: String = "commons.config_db_url"
  val ConfigDbUser: String = "commons.config_db_user"
  val ConfigDbPass: String = "commons.config_db_pass"
  val TransformedPrefixConfig: String = "commons.transformed_prefix"
  val IntermediatePrefixConfig: String = "commons.intermediate_prefix"
  val TransformedPrefixVersionedConfig: String = "commons.transformed_prefix_versioned"
  val FsPrefix: String = "fs_prefix"
  val BeginningOfTimeConfig = "commons.beginning_of_time"

  val MysqlJdbcDriver: String = "com.mysql.jdbc.Driver"
  val PrestoJdbcDriver = "com.facebook.presto.jdbc.PrestoDriver"
  val PostgresJdbcDriver = "org.postgresql.Driver"
  val SqlServerJdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
  val OracleJdbcDriver = "oracle.jdbc.OracleDriver"

  //binlog task meta
  val MultiPrimaryKey: String = "multi_primary_key"

  // CDC consumer config
  val BinlogSourceFileNum: String = "binlog_source_file_num"
  val BinlogOrderColumns: Seq[String] = Seq("binlog_timestamp_sec", "binlog_source_file_num", "binlog_source_pos")
  val BinlogOperation: String = "binlog_operation"
  val BinlogOrderColumnDefaultValues: Seq[Any] = Seq(0L, 0L, 0L)
  val LagCheckMaxRetries = 3
  val BackOffTime: List[Int] = List(3, 8, 21)
  val MaxAllowedLag: Int = 3600 // seconds
  val CDCBinlogMysqlThreshold = 10 // minutes

  // S3 source input config
  val S3Source = "s3_source_input"
  val S3SourceInputPath = "s3_source_input_path"
  val MigrationBatchTime = "migration_batch_time"
  val MigrationInputPath = "migration_input_path"
  val S3InputTimeFormat = "yyyyMMddHH"
  val SQLTransformation = "sql_transformation"
  val IgnoreColumns = "ignored_columns"
  val PreserveTypeColumns = "preserve_type_columns"
  val S3JobGranularity = "hour"
  val Iso8601TimeFormat = "yyyy-MM-dd'T'HH:mm:ss"
  val UpstreamType = "upstream_type"

  // String Constants
  val Comma = ","
  val EmptyString = ""
  val Underscore = "_"
  val Dash = "-"
  val BackQuote = "`"
  val DoubleQuote = "\""
  val LeftSquareBracket = "["
  val RightSquareBracket = "]"
  val NewLine = "\n"
  val Dot = '.'
  val VerticalBar = "|"
  val Hyphen = "-"
  val Equals = "="
  val Null = null
  val AtSign = "@"
  val AzurefsScheme = "abfss://"
  val AzureDomainName = "dfs.core.windows.net"
  val S3Scheme = "s3://"
  val Temp = "_temp"
  val TransformedPrefix = "datalake/transformed"
  val TopicValidationIntermediateRecordColumnName = "noOfRecordsWrittenInIntermediateDirectory"
  val IntermediatePrefix = "datalake/intermediate"
  val BucketNameRegex = """(?<=://)([a-z0-9.-]{3,63})(?=/|$)"""
  val DataQualityBucket = "grab-mercury-test"
  val DataQualityPrefix = "datalake/data-quality"
  val DataQualityParquetSource = "parquet_source"
  val DataQualityMySQLSource = "mysql_source"
  val AzureIntermediatePrefix = "data__loader__internal/intermediate"
  val TransformedPrefixVersioned = "datalake/transformed-versioned"
  val AmazonAwsLogRequestKeyword = "com.amazonaws.request"
  val StreamingOutputRealtimePath = "real-time-events"
  val FileScheme: String = "file://"
  val CopyToTransformRetryTime = "3"
  val BeginningOfTime = "1970-01-01:00:00:00"
  val TopicTag = "topic:"
  val VersionPathPrefix = "version_path_prefix"
  val VersionPathPrefixPart = "version_path_prefix_part"
  val IsTimestampMillis = "is_timestamp_millis"

  val DeTS = "de_ts"
  val DeTSAzure = "de-ts"
  val DeTSPart = "de_ts="
  val YearPart = "year="
  val Year = "year"
  val Month = "month"
  val MonthPart = "month="
  val Day = "day"
  val DayPart = "day="
  val Hour = "hour"
  val Minute = "minute"
  val HourPart = "hour="
  val MinutePart = "minute="
  val DatePad = "0"
  val Rank = "rank"
  val Avro = "avro"
  val Parquet = "parquet"
  val SaltColumn = "salt"
  val SparkDateTimeFormat = "yyyy-MM-dd HH:mm:ss"
  val HypenTimeFormat = "yyyy-MM-dd-HH"

  // DynamoDB
  object DynamoDB {
    val TYPE_CLASS = "type_class"
    val ROLE_ARN = "rolearn"
    val REGION = "region"
    val READ_PARTITIONS = "readPartitions"
  }

  // AWS Dynamodb Connector config constants
  val DynamoDbAccessKeyId: String = "dynamodb.awsAccessKeyId"
  val DynamoDbSecretAccessKey: String = "dynamodb.awsSecretAccessKey"
  val DynamoDbSessionToken: String = "dynamodb.awsSessionToken"
  val DynamoDbTableName: String = "dynamodb.input.tableName"
  val DynamoDbThroughPutRead: String = "dynamodb.throughput.read"
  val DynamoDbRoleArn: String = "arn:aws:iam::465027161316:role/subacc-grab-data-prd"
  val DynamoDbSessionDuration: Int = 3600
  val DynamoDbRole = "ddb_role"

  object ElasticSearch {
    val INDEX = "index"
    val INDEX_PATTERN = "index_pattern"
    val TYPE = "type"
    val SCROLL_SIZE = "scroll_size"
    val START_TS = "@start_ts"
    val END_TS = "@end_ts"
    val TS_FORMAT = "ts_format"
    val ADD_PARTITION_COLUMN = "add_partition_column"
  }

  //COST Related
  val HUGO_UNIT: String = "HUGO_UNIT"
  // Spark conf
  val SPARK_APP_NAME = "spark.app.name"
  val BasePath = "basePath"

  // Trailblazer mysql tables
  val STREAMING_COMPACTION_TOPIC_METADATA_INFO = "streaming_compaction_topic_metadata_info"
  val STREAMING_TOPIC_METADATA_INFO = "streaming_topic_metadata_info"
  val JOB_METADATA_INFO = "job_metadata_info"
  val STREAMING_COMPACTION_JOB_METADATA_INFO = "streaming_compaction_job_metadata_info"
  val STREAMING_COMPACTION_TOPIC_VALIDATION_INFO = "streaming_compaction_topic_validation_info"

  // Application Logging
  val LogsPatternLayout = "%d{DEFAULT} %p [%t] %c:%m%n%throwable"
  val InMemoryLogSize = 10000 // number of most recent lines of log allowed in-memory

  // Mercury db tables
  val MercuryJobRuns: String = "job_runs"
  val MercuryJobInfo: String = "job_info"
  // Test constants
  val LocalPort: String = "local_port"
  val TestDbName: String = "test"
  val TestDirectory: String = "grab-mercury-test"

  // Sort column in test
  val DefaultSortColumn: String = "id"

  // Meta data related
  val NUMBER_OF_PARTITIONS_META_KEY = "number_of_partitions"
  val PARTITION_REGISTRATION_META_KEY = "partition_registration"
  val PARTITION_DELETION_META_KEY = "partition_deletion"
  val DATA_INGESTION_META_KEY = "data_ingestion"
  val LOADER_INIT_META_KEY = "loader_init"
  val MIN_MAX_CREATED_META_KEY = "min_max_created"
  val MIN_MAX_UPDATED_META_KEY = "min_max_updated"

}
