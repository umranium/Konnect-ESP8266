package com.umranium.esp8266.konnect.presentation.launch

import com.umranium.esp8266.konnect.presentation.common.BaseActivityController
import com.umranium.esp8266.konnect.utils.rx.LocalBus

/**
 * Controller for the [LaunchActivity]
 */
class LaunchActivityController(val surface: Surface, activityBus: LocalBus): BaseActivityController(activityBus) {


  fun onCreate() {
    surface.proceedToNodeScanActivity()
  }

  interface Surface {
    fun proceedToNodeScanActivity()
  }

}
