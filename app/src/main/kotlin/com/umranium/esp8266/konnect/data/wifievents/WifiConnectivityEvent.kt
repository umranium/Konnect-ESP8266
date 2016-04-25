package com.umranium.esp8266.konnect.data.wifievents

import android.net.wifi.WifiInfo

/**
 * A WiFi connectivity event.

 * @author umran
 */
sealed class WifiConnectivityEvent : WifiEvent() {
  /**
   * WiFi connected event.

   * @author umran
   */
  class WifiConnected(val wifiInfo: WifiInfo) : WifiConnectivityEvent()

  class WifiDisconnected : WifiConnectivityEvent()
}
