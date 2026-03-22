package com.grab.mercury.config

import com.grab.mercury.constants.Constants.{ENV, RUNTIME, TRIGGER_BY}

trait DriverConfBase {
    def jobName: String

    def taskId: String

    def env: ENV.ENV

    def triggerBy: TRIGGER_BY.TRIGGER_BY

    def runtime: RUNTIME.RUNTIME

    def mainClass: Option[String]

    def jobArgs: Option[String]
}