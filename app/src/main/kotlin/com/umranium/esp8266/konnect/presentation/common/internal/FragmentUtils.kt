package com.umranium.esp8266.konnect.presentation.common.internal

import android.app.Activity
import com.umranium.esp8266.konnect.presentation.common.BaseActivity
import com.umranium.esp8266.konnect.presentation.common.BaseActivityController

/**
 * Obtain activity's controller (if set), throw exception if not set.
 */
fun getBaseActivity(activity: Activity?): BaseActivity<*> {
  var parent = activity ?: throw IllegalStateException("Fragment's parent activity is null")
  if (parent !is BaseActivity<*>) {
    throw IllegalStateException("Fragment's parent activity is not " + BaseActivity::class.java.simpleName)
  }
  return parent
}

/**
 * Obtain activity's controller (if set), throw exception if not set.
 */
fun controllerFromBaseActivity(activity: Activity?): BaseActivityController {
  return getBaseActivity(activity).controller
}

