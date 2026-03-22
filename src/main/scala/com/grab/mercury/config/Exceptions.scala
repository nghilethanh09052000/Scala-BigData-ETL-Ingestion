package com.grab.mercury.config

import com.grab.mercury.exceptions.DataLoaderGeneralException

object Exceptions {

  final case class UnsupportedEnvException(env: String) extends Exception(s"$env not supported in this context") with DataLoaderGeneralException

  final case class LoaderConfigKeyNotFoundException(key: String) extends Exception(s"loader config key: $key not found in local config map")
    with DataLoaderGeneralException

  final case class InvalidLoaderConfigKeyException(key: String, message: String)
    extends IllegalArgumentException(s"value for loaderConfig key: $key is invalid - $message") with DataLoaderGeneralException

  final case class DataloaderUnsupportedFeatureException(feature: String)
    extends UnsupportedOperationException(s"$feature is not supported") with DataLoaderGeneralException
}