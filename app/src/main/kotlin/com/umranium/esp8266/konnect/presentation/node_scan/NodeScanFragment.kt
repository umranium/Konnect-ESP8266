package com.umranium.esp8266.konnect.presentation.node_scan

import android.Manifest.permission
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.umranium.esp8266.konnect.R
import com.umranium.esp8266.konnect.R.string
import com.umranium.esp8266.konnect.data.wifievents.WifiEvents
import com.umranium.esp8266.konnect.presentation.common.BaseFragment
import com.umranium.esp8266.konnect.presentation.node_scan.helpers.ScannedAccessPointExtracter
import com.umranium.esp8266.konnect.presentation.utils.permissions.PermissionHandler
import com.umranium.esp8266.konnect.presentation.utils.permissions.PermissionHandler.Details

const val LOCATION_PERMISSION_REQUEST_CODE = 1

/**
 * The fragment that shows the ESP8266 access points found through a WiFi scan
 */
class NodeScanFragment : BaseFragment<NodeScanFragmentController>(), NodeScanFragmentController.Surface {

  private val NODE_MCU_AP_FMT = "ESP.*"

  override val controller = NodeScanFragmentController(
    localBusProvider = { activityController.activityBus },
    surface = this,
    wifiEventDispatcher = WifiEvents.dispatcher,
    wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager,
    scannedAccessPointExtracter = ScannedAccessPointExtracter(NODE_MCU_AP_FMT.toRegex(),
      context.getSystemService(Context.WIFI_SERVICE) as WifiManager)
  )

  private lateinit var scanningLayout: ViewGroup
  private lateinit var noResultsLayout: ViewGroup
  private lateinit var resultsListLayout: ViewGroup
  private lateinit var permissionHandler: PermissionHandler
  private lateinit var startWifiScan: PermissionHandler.Checker
  private val listAdapter = AccessPointListAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    var view = inflater.inflate(R.layout.fragment_node_scan, container, false)

    scanningLayout = view.findViewById(R.id.layout_scanning) as ViewGroup
    noResultsLayout = view.findViewById(R.id.layout_no_results) as ViewGroup
    resultsListLayout = view.findViewById(R.id.layout_results_list) as ViewGroup

    var list = view.findViewById(R.id.ap_list) as RecyclerView
    list.layoutManager = LinearLayoutManager(context)
    list.adapter = listAdapter

    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    permissionHandler = PermissionHandler(this)
    startWifiScan = permissionHandler.register(
      Details(
        requestCode = LOCATION_PERMISSION_REQUEST_CODE,
        payloadFunc = { controller.onStart(listAdapter.clickEvents) },
        permissions = arrayOf(permission.ACCESS_COARSE_LOCATION),
        permissionRationaleTitle = string.course_location_permission_rationale_title,
        permissionRationaleMessage = string.course_location_permission_rationale_message,
        deniedFunc = { handleLackOfCourseLocationPermission() },
        neverAskFunc = { handleLackOfCourseLocationPermission() }
      )
    )
  }

  override fun onStart() {
    super.onStart()
    startWifiScan()
  }

  override fun onStop() {
    super.onStop()
    controller.onStop()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    permissionHandler.onRequestPermissionsResult(requestCode, grantResults)
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  private fun handleLackOfCourseLocationPermission() {
    Toast.makeText(context, R.string.course_location_permission_denied_message, Toast.LENGTH_LONG)
      .show();
    activity.finish();
  }

  override fun showScanningLayout() {
    scanningLayout.visibility = View.VISIBLE
    noResultsLayout.visibility = View.GONE
    resultsListLayout.visibility = View.GONE
  }

  override fun startAccessPointConfigActivity(accessPoint: ScannedAccessPoint) {
    Toast.makeText(context, "Clicked ${accessPoint.ssid}", Toast.LENGTH_SHORT)
  }

  override fun showNoResultsLayout() {
    scanningLayout.visibility = View.GONE
    noResultsLayout.visibility = View.VISIBLE
    resultsListLayout.visibility = View.GONE
  }

  override fun showResultsListLayout(accessPoints: Collection<ScannedAccessPoint>) {
    scanningLayout.visibility = View.GONE
    noResultsLayout.visibility = View.GONE
    resultsListLayout.visibility = View.VISIBLE
    listAdapter.populate(accessPoints)
  }

  override fun updateResultsList(accessPoints: Collection<ScannedAccessPoint>) {
    listAdapter.populate(accessPoints)
  }
}
