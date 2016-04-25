package com.umranium.esp8266.konnect.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.umranium.esp8266.konnect.data.wifievents.WifiEvents

/**
 * Receives WiFi events from the system and dispatches them to the app.
 *
 * @author umran
 */
class WifiStateChangeReceiver : BroadcastReceiver() {

  private val dispatcher = WifiEvents.dispatcher

  override fun onReceive(context: Context, intent: Intent) {
    var wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    when (intent.action) {
      WifiManager.WIFI_STATE_CHANGED_ACTION -> {
        if (wifiManager.isWifiEnabled) {
          dispatcher.emitEnabled()
        } else {
          dispatcher.emitDisabled()
        }
      }

      WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
        var ntwkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (ntwkInfo.isConnected) {
          var wifiInfo = if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO)
          } else {
            wifiManager.connectionInfo
          }
          dispatcher.emitConnected(wifiInfo)
        } else {
          dispatcher.emitDisconnected()
        }
      }

      WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
        dispatcher.emitScanComplete()
      }
    }
  }
}