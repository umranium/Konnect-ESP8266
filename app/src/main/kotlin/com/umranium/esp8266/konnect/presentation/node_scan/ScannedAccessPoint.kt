package com.umranium.esp8266.konnect.presentation.node_scan

/**
 * A scanned access point.
 */
data class ScannedAccessPoint(var id: Long, val ssid: String, val signalStrength: Int) {

  constructor(bssid: String, ssid: String, signalStrength: Int) : this(bssidToLong(bssid), ssid, signalStrength)

}

private val BSSID_REGEX = Regex("\\p{XDigit}{2}(?::\\p{XDigit}{2})*")

private fun bssidToLong(bssid: String): Long {
  // ensure that it matches XX:XX:XX:...:XX
  if (!bssid.matches(BSSID_REGEX)) return -1L

  var sections = bssid.split(':')
  var value = 0L;
  for (i in 0..sections.lastIndex) {
    var sectionValue = Integer.parseInt(sections[i], 16)

    value = value shl 8; // move by 1 byte
    value = value or (sectionValue and 0xFF).toLong()
  }

  return value
}
