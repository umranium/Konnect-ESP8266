package com.umranium.esp8266.konnect.utils

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import android.os.SystemClock

import java.util.concurrent.TimeUnit

object BootTimeUtils {

  val diffBootFromUtc: Long
    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    get() = System.currentTimeMillis() - TimeUnit.NANOSECONDS.toMillis(SystemClock.elapsedRealtimeNanos())

  fun timeSinceBootToUtc(timeSinceBoot: Long, diffBootFromUtc: Long): Long {
    return timeSinceBoot + diffBootFromUtc
  }

  fun utcToTimeSinceBoot(utcTime: Long, diffBootFromUtc: Long): Long {
    return utcTime + diffBootFromUtc
  }
}