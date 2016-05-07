package com.umranium.esp8266.konnect.presentation.node_scan

import android.net.wifi.WifiManager
import com.umranium.esp8266.konnect.data.wifievents.WifiEventDispatcher
import com.umranium.esp8266.konnect.presentation.node_scan.helpers.ScannedAccessPointExtracter
import com.umranium.esp8266.konnect.utils.rx.LocalBus
import mockit.Expectations
import mockit.Injectable
import mockit.Verifications
import mockit.integration.junit4.JMockit
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rx.Observable
import rx.subjects.PublishSubject

@RunWith(JMockit::class)
class NodeScanFragmentControllerTest {

  private lateinit var localBus: LocalBus
  @Injectable
  private lateinit var surface: NodeScanFragmentController.Surface
  private lateinit var wifiEventDispatcher: WifiEventDispatcher
  @Injectable
  private lateinit var wifiManager: WifiManager
  @Injectable
  private lateinit var scannedAccessPointExtracter: ScannedAccessPointExtracter
  private lateinit var controller: NodeScanFragmentController

  @Before
  fun setUp() {
    localBus = LocalBus()
    wifiEventDispatcher = WifiEventDispatcher()
    controller = NodeScanFragmentController({ localBus }, surface,
      wifiEventDispatcher, wifiManager, scannedAccessPointExtracter)
  }

  @Test
  fun onStart_initiatesScan() {
    // when
    controller.onStart(Observable.never())

    // then
    object : Verifications() {
      init {
        wifiManager.startScan()
      }
    }
  }

  @Test
  fun onStart_restartsScanAfterComplete() {
    // when
    controller.onStart(Observable.never())
    wifiEventDispatcher.emitScanComplete()

    // then
    object : Verifications() {
      init {
        wifiManager.startScan()
        times = 2
      }
    }
  }

  @Test
  fun onStart_showsScanningLayout() {
    // when
    controller.onStart(Observable.never())

    // then
    object : Verifications() {
      init {
        surface.showScanningLayout()
      }
    }
  }

  @Test
  fun on_accessPointClickEvent_startsAccessPointConfigActivity() {
    // when
    val scannedAccessPoint = ScannedAccessPoint(0L, "", 0)
    controller.onStart(Observable.just(scannedAccessPoint))

    // then
    object : Verifications() {
      init {
        surface.startAccessPointConfigActivity(scannedAccessPoint)
      }
    }
  }

  @Test
  fun on_scanComplete_displaysScanResults() {
    // when
    val accessPoint = ScannedAccessPoint(0, "", 0)
    object : Expectations() {
      init {
        scannedAccessPointExtracter.extractAccessPoints(anyLong)
        result = arrayListOf(accessPoint)
      }
    }

    controller.onStart(Observable.never())
    wifiEventDispatcher.emitScanComplete()

    // then
    object : Verifications() {
      init {
        surface.showResultsListLayout(arrayListOf(accessPoint))
      }
    }
  }

  @Test
  fun onStop_doesNotStartAccessPointConfigActivity() {
    // when
    val subject = PublishSubject.create<ScannedAccessPoint>()
    val scannedAccessPoint = ScannedAccessPoint(0L, "", 0)
    controller.onStart(subject.asObservable())
    controller.onStop()
    subject.onNext(scannedAccessPoint)

    // then
    object : Verifications() {
      init {
        surface.startAccessPointConfigActivity(scannedAccessPoint)
        times = 0
      }
    }
  }

}