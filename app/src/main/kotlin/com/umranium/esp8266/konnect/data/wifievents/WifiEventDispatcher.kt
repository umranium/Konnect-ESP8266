package com.umranium.esp8266.konnect.data.wifievents

import android.net.wifi.WifiInfo
import com.umranium.esp8266.konnect.data.wifievents.WifiConnectivityEvent.WifiConnected
import com.umranium.esp8266.konnect.data.wifievents.WifiConnectivityEvent.WifiDisconnected
import com.umranium.esp8266.konnect.data.wifievents.WifiStateEvent.WifiDisabled
import com.umranium.esp8266.konnect.data.wifievents.WifiStateEvent.WifiEnabled
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Class that serves as a receiver and dispatcher of
 * WiFi/Network events.
 *
 * @author umran
 */
class WifiEventDispatcher {

  private val _events = PublishSubject.create<WifiEvent>();

  val events: Observable<WifiEvent>
    get() = _events.asObservable()

  val connectivityEvents: Observable<WifiConnectivityEvent>
    get() = events
        .ofType(WifiConnectivityEvent::class.java)
        .distinctUntilChanged()

  val connected: Observable<WifiConnected>
    get() = events
        .ofType(WifiConnected::class.java)

  val disconnected: Observable<WifiDisconnected>
    get() = events
        .ofType(WifiDisconnected::class.java)

  val stateEvents: Observable<WifiStateEvent>
    get() = events
        .ofType(WifiStateEvent::class.java)

  val enabled: Observable<WifiEnabled>
    get() = events
        .ofType(WifiEnabled::class.java)

  val disabled: Observable<WifiDisabled>
    get() = events
        .ofType(WifiDisabled::class.java)

  val scanComplete: Observable<WifiScanComplete>
    get() = events
        .ofType(WifiScanComplete::class.java)

  private val disconnectedValue = WifiDisconnected()
  private val enabledValue = WifiEnabled()
  private val disabledValue = WifiDisabled()
  private val scanCompleteValue = WifiScanComplete()

  fun emitConnected(wifiInfo: WifiInfo) = _events.onNext(WifiConnected(wifiInfo))

  fun emitDisconnected() = _events.onNext(disconnectedValue)

  fun emitEnabled() = _events.onNext(enabledValue)

  fun emitDisabled() = _events.onNext(disabledValue)

  fun emitScanComplete() = _events.onNext(scanCompleteValue)

}