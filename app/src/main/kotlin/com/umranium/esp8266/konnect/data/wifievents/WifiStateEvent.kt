package com.umranium.esp8266.konnect.data.wifievents

/**
 * Common ancestor of all WiFi state events.

 * @author umran
 */
sealed class WifiStateEvent : WifiEvent() {

  /**
   * WiFi enabled event.

   * @author umran
   */
  class WifiEnabled : WifiStateEvent()

  /**
   * WiFi disabled event.

   * @author umran
   */
  class WifiDisabled : WifiStateEvent()

}
