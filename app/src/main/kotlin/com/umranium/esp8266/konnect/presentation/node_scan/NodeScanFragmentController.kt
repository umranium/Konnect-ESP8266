package com.umranium.esp8266.konnect.presentation.node_scan

import android.net.wifi.WifiManager
import com.umranium.esp8266.konnect.data.wifievents.WifiEventDispatcher
import com.umranium.esp8266.konnect.presentation.common.BaseFragmentController
import com.umranium.esp8266.konnect.presentation.node_scan.helpers.ScannedAccessPointExtracter
import com.umranium.esp8266.konnect.utils.rx.LocalBus
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 * Controls the node scan UI
 */
class NodeScanFragmentController(
  localBusProvider: () -> LocalBus,
  private val surface: Surface,
  private val wifiEventDispatcher: WifiEventDispatcher,
  private val wifiManager: WifiManager,
  private val scannedAccessPointExtracter: ScannedAccessPointExtracter) :
  BaseFragmentController(localBusProvider) {

  private lateinit var clickEvents: Subscription
  private var lastScanRequestTimestamp: Long = -1

  fun onStart(accessPointClickEvents: Observable<ScannedAccessPoint>) {
    startScan()

    // whenever a scan completes, initiate another scan
    wifiEventDispatcher
      .scanComplete
      .subscribe {
        scanComplete()
        startScan()
      }

    // when an access point is clicked, show access-point config activity
    clickEvents = accessPointClickEvents.subscribe {
      surface.startAccessPointConfigActivity(it)
    }

  }

  fun onStop() {
    clickEvents.unsubscribe()
  }

  private fun startScan() {
    if (lastScanRequestTimestamp < 0) {
      // show scanning layout
      surface.showScanningLayout()
    }
    wifiManager.startScan()
    lastScanRequestTimestamp = System.currentTimeMillis()
  }

  private fun scanComplete() {
    var accessPoints = scannedAccessPointExtracter.extractAccessPoints(lastScanRequestTimestamp)
    if (accessPoints.isEmpty()) {
      surface.showNoResultsLayout()
    } else {
      surface.showResultsListLayout(accessPoints)
    }
  }

  interface Surface {

    fun showScanningLayout()

    fun startAccessPointConfigActivity(accessPoint: ScannedAccessPoint)

    fun showNoResultsLayout()

    fun showResultsListLayout(accessPoints: Collection<ScannedAccessPoint>)

    fun updateResultsList(accessPoints: Collection<ScannedAccessPoint>)

  }

}

