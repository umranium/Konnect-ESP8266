package com.umranium.esp8266.konnect.presentation.utils.permissions

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import com.umranium.esp8266.konnect.presentation.common.BaseDialogFragment
import com.umranium.esp8266.konnect.utils.rx.BaseLocalMessage

/**
 * Dialog for showing permissions rationale
 */

private const val PARAM_REQUEST_CODE = "request_code"
private const val PARAM_TITLE = "title"
private const val PARAM_MESSAGE = "message"

class PermissionsRationaleDialog : BaseDialogFragment() {

  data class Result(val requestCode: Int, val proceed: Boolean) : BaseLocalMessage

  companion object {

    fun newInstance(requestCode: Int,
                    @StringRes title: Int,
                    @StringRes message: Int): PermissionsRationaleDialog {
      val frag = PermissionsRationaleDialog()
      val args = Bundle()
      args.putInt(PARAM_REQUEST_CODE, requestCode)
      args.putInt(PARAM_TITLE, title)
      args.putInt(PARAM_MESSAGE, message)
      frag.arguments = args
      return frag
    }

  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val args = arguments ?: throw IllegalStateException("Arguments not provided!")

    @StringRes val title = args.getInt(PARAM_TITLE)
    @StringRes val message = args.getInt(PARAM_MESSAGE)
    val requestCode = args.getInt(PARAM_REQUEST_CODE)

    return AlertDialog.Builder(activity)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
          activityController.activityBus.emit(Result(requestCode, true))
        }
        .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->
          activityController.activityBus.emit(Result(requestCode, false))
        }
        .create()
  }

}
