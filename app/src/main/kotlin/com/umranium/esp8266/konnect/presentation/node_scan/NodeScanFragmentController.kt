package com.umranium.esp8266.konnect.presentation.node_scan

import android.os.Bundle
import com.umranium.esp8266.konnect.presentation.common.BaseFragmentController
import com.umranium.esp8266.konnect.utils.rx.LocalBus

/**
 * Controls the node scan UI
 */
class NodeScanFragmentController(val surface: Surface, localBusProvider: () -> LocalBus) :
    BaseFragmentController(localBusProvider) {

  fun startWifiScan() {
    surface.showScanningLayout()
  }

  fun onStop() {

  }

  fun onDestroy() {

  }


  interface Surface {

    fun showScanningLayout()

    fun showNoResultsLayout()

    fun showResultsListLayout(accessPoints: Collection<ScannedAccessPoint>)

    fun updateResultsList(accessPoints: Collection<ScannedAccessPoint>)

  }

}

