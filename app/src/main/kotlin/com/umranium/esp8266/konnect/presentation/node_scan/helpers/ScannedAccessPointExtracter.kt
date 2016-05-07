package com.umranium.esp8266.konnect.presentation.node_scan.helpers

import android.net.wifi.WifiManager
import android.os.Build
import com.umranium.esp8266.konnect.presentation.node_scan.ScannedAccessPoint
import com.umranium.esp8266.konnect.utils.BootTimeUtils
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Extracts scanned access point records from WiFi scan results matching a particular SSID
 * format and for SDK>17 only those that have been obtained during the recent scan.
 *
 * @author umran
 */
class ScannedAccessPointExtracter(val nodeMcuApFmt: Regex, val wifiManager: WifiManager) {

  fun extractAccessPoints(lastRequestTimestamp: Long): List<ScannedAccessPoint> {
    var scanRequestBootTimeMicros: Long = 0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      scanRequestBootTimeMicros = TimeUnit.MILLISECONDS.toMicros(
        BootTimeUtils.utcToTimeSinceBoot(lastRequestTimestamp,
          BootTimeUtils.diffBootFromUtc))
    }

    val accessPoints = ArrayList<ScannedAccessPoint>()

    for (scanResult in wifiManager.scanResults) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        // if possible, filter out any access-points not discovered during the last scan
        if (scanResult.timestamp >= scanRequestBootTimeMicros) {
          continue
        }
      }

      val name = scanResult.SSID
      if (!name.matches(nodeMcuApFmt)) {
        continue
      }

      val level = WifiManager.calculateSignalLevel(scanResult.level, 100)

      accessPoints.add(ScannedAccessPoint(scanResult.BSSID, name, level))
    }

    return accessPoints
  }

}