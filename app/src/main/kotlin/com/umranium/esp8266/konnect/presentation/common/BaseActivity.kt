package com.umranium.esp8266.konnect.presentation.common

import android.support.v7.app.AppCompatActivity

/**
 * Base activity of all activities in the app
 */
abstract class BaseActivity<ControllerType: BaseActivityController>: AppCompatActivity() {

  /**
   * The controller for this activity.
   */
  abstract val controller: ControllerType

}
