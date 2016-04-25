package com.umranium.esp8266.konnect.presentation.node_scan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.umranium.esp8266.konnect.R
import com.umranium.esp8266.konnect.presentation.common.BaseActivity
import com.umranium.esp8266.konnect.utils.rx.LocalBus

class NodeScanActivity: BaseActivity<NodeScanActivityController>() {

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, NodeScanActivity::class.java)
    }
  }

  override val controller = NodeScanActivityController(LocalBus())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_node_scan)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_node_scan, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true
    }

    return super.onOptionsItemSelected(item)
  }

}

