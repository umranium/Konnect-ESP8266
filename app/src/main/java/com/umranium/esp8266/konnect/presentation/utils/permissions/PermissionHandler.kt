package com.umranium.esp8266.konnect.presentation.utils.permissions

import android.content.Context
import android.os.Build.VERSION_CODES
import android.support.annotation.StringRes
import android.support.v4.app.FragmentManager
import com.umranium.esp8266.konnect.presentation.common.BaseActivity
import com.umranium.esp8266.konnect.presentation.common.BaseFragment
import com.umranium.esp8266.konnect.presentation.utils.permissions.internal.PermissionUtils
import com.umranium.esp8266.konnect.utils.rx.LocalBus
import java.util.*

class PermissionHandler private constructor(val targetActivity: BaseActivity<*>?, val targetFragment: BaseFragment<*>?) {

  constructor(activity: BaseActivity<*>): this(activity, null)

  constructor(fragment: BaseFragment<*>): this(null, fragment)

  private val requestCodeToCheckerMap = HashMap<Int, Checker>()

  private val activityBus: LocalBus
    get() {
      if (targetFragment != null) {
        return targetFragment.controller.activityBus
      } else if (targetActivity != null) {
        return targetActivity.controller.activityBus
      } else {
        throw neitherFragmentNorActivityProvidedException()
      }
    }

  private val context: Context
    get() {
      if (targetFragment != null) {
        return targetFragment.context
      } else if (targetActivity != null) {
        return targetActivity
      } else {
        throw neitherFragmentNorActivityProvidedException()
      }
    }

  private val activity: BaseActivity<*>
    get() {
      if (targetFragment != null) {
        return targetFragment.baseActivity
      } else if (targetActivity != null) {
        return targetActivity
      } else {
        throw neitherFragmentNorActivityProvidedException()
      }
    }

  private val fragmentManager: FragmentManager
    get() {
      if (targetFragment != null) {
        return targetFragment.childFragmentManager
      } else if (targetActivity != null) {
        return targetActivity.supportFragmentManager
      } else {
        throw neitherFragmentNorActivityProvidedException()
      }
    }

  init {
    activityBus
        .wait(PermissionsRationaleDialog.Result::class.java)
        .subscribe { result ->
          if (requestCodeToCheckerMap.containsKey(result.requestCode)) {
            requestCodeToCheckerMap[result.requestCode]!!.handlePermissionRationaleDialogResult(result.proceed)
          }
        }
  }

  fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
    if (requestCodeToCheckerMap.containsKey(requestCode)) {
      requestCodeToCheckerMap[requestCode]!!.handlePermissionRequestResult(grantResults)
    }
  }

  fun register(details: Details): Checker {
    if (requestCodeToCheckerMap.containsKey(details.requestCode)) {
      throw IllegalStateException("Request code ${details.requestCode} is already used!")
    }
    var checker = Checker(details)
    requestCodeToCheckerMap.put(details.requestCode, checker)
    return checker
  }

  private fun neitherFragmentNorActivityProvidedException() =
      IllegalStateException("Neither fragment nor activity have been provided")


  data class Details(val requestCode: Int,
                     val payloadFunc: () -> Unit,
                     val permissions: Array<String>,
                     @StringRes val permissionRationaleTitle: Int,
                     @StringRes val permissionRationaleMessage: Int,
                     val deniedFunc: () -> Unit,
                     val neverAskFunc: () -> Unit)

  /**
   * Ensures permissions checks and permission handling is done before executing a function.
   */
  inner class Checker(val details: Details) {

    private val dlgTag by lazy {
      details.permissions.fold(StringBuilder(), { cumm, curr -> cumm.append(curr) }).toString()
    }

    operator fun invoke() {
      if (PermissionUtils.hasSelfPermissions(context, *details.permissions)) {
        details.payloadFunc()
      } else {
        if (PermissionUtils.shouldShowRequestPermissionRationale(activity, *details.permissions)) {
          var dlg = PermissionsRationaleDialog.newInstance(details.requestCode,
              details.permissionRationaleTitle, details.permissionRationaleMessage)
          targetFragment?.childFragmentManager
          dlg.show(fragmentManager, dlgTag)
        } else {
          requestPermissions()
        }
      }
    }

    fun handlePermissionRequestResult(grantResults: IntArray) {
      if (PermissionUtils.getTargetSdkVersion(context) < VERSION_CODES.M &&
          !PermissionUtils.hasSelfPermissions(context, *details.permissions)) {
        details.deniedFunc()
        return;
      }

      if (PermissionUtils.verifyPermissions(*grantResults)) {
        details.payloadFunc()
      } else {
        if (!PermissionUtils.shouldShowRequestPermissionRationale(activity, *details.permissions)) {
          details.neverAskFunc()
        } else {
          details.deniedFunc()
        }
      }
    }

    fun handlePermissionRationaleDialogResult(proceed: Boolean) {
      if (proceed) {
        requestPermissions()
      } else {
        details.deniedFunc()
      }
    }

    private fun requestPermissions() {
      if (targetFragment != null) {
        targetFragment.requestPermissions(details.permissions, details.requestCode)
      } else if (targetActivity != null) {
        targetActivity.requestPermissions(details.permissions, details.requestCode)
      } else {
        throw neitherFragmentNorActivityProvidedException()
      }
    }
  }

}
