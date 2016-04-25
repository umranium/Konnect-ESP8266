package com.umranium.esp8266.konnect.presentation.common

import com.umranium.esp8266.konnect.utils.rx.LocalBus

/**
 * The base controller for all activities in the app.
 */
open class BaseActivityController(
    /**
     * A local bus that allows communication of components that are within the same activity.
     */
    val activityBus: LocalBus) {

}
