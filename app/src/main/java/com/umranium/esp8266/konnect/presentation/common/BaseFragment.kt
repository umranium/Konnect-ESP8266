package com.umranium.esp8266.konnect.presentation.common

import android.support.v4.app.Fragment
import com.umranium.esp8266.konnect.presentation.common.internal.controllerFromBaseActivity
import com.umranium.esp8266.konnect.presentation.common.internal.getBaseActivity

/**
 * Base fragment of all fragments in the app
 */
abstract class BaseFragment<ControllerType: BaseFragmentController>: Fragment() {

  /**
   * The controller for this fragment.
   */
  abstract val controller: ControllerType

  /**
   * The controller for this fragment's activity.
   * An exception is thrown if this field is used while this fragment's activity is not set.
   */
  val activityController: BaseActivityController
    get() = controllerFromBaseActivity(activity)

  val baseActivity: BaseActivity<*>
    get() = getBaseActivity(activity)

}
