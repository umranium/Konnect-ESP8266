package com.umranium.esp8266.konnect.presentation.common

import android.support.v7.app.AppCompatDialogFragment
import com.umranium.esp8266.konnect.presentation.common.internal.controllerFromBaseActivity

/**
 * Base class of all [DialogFragment]s in the app
 */
open class BaseDialogFragment: AppCompatDialogFragment() {

  /**
   * The controller for this fragment's activity.
   * An exception is thrown if this field is used while this fragment's activity is not set.
   */
  val activityController: BaseActivityController
    get() = controllerFromBaseActivity(activity)

}
