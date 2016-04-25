package com.umranium.esp8266.konnect.presentation.launch

import android.os.Bundle
import com.umranium.esp8266.konnect.presentation.common.BaseActivity
import com.umranium.esp8266.konnect.presentation.node_scan.NodeScanActivity
import com.umranium.esp8266.konnect.utils.rx.LocalBus

class LaunchActivity: BaseActivity<LaunchActivityController>(), LaunchActivityController.Surface {

  override val controller = LaunchActivityController(this, LocalBus())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    controller.onCreate()
  }

  override fun proceedToNodeScanActivity() {
    startActivity(NodeScanActivity.createIntent(this))
    finish()
  }

}
