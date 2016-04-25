package com.umranium.esp8266.konnect.utils.rx

import org.junit.Test

import rx.observers.TestSubscriber

const val INITIAL_VALUE = 1
const val NEXT_VALUE = 2
const val NEXT_DOUBLE_VALUE = 2.0

class ObservableValueTest {

  @Test
  fun constructor_onSubscribeEmitLast_emitsInitialItemOnSubscription() {
    val value = ObservableValue(true, INITIAL_VALUE)
    val testSubscriber = TestSubscriber<Int>()

    value
        .observable
        .subscribe(testSubscriber)

    testSubscriber.assertValue(INITIAL_VALUE)
  }

  @Test
  fun constructor_onSubscribeEmitLast_emitsPreviousItemOnSubscription() {
    val value = ObservableValue(true, INITIAL_VALUE)
    val testSubscriber = TestSubscriber<Int>()
    value.value = NEXT_VALUE

    value
        .observable
        .subscribe(testSubscriber)

    testSubscriber.assertValue(NEXT_VALUE)
  }

  @Test
  fun constructor_noOnSubscribeEmitLast_emitsNoItemOnSubscription() {
    val value = ObservableValue(false, INITIAL_VALUE)
    val testSubscriber = TestSubscriber<Int>()

    value
        .observable
        .subscribe(testSubscriber)

    testSubscriber.assertNoValues()
  }

  @Test
  fun observable_afterSetValue_emitsSetValue() {
    val value = ObservableValue(false, INITIAL_VALUE)
    val testSubscriber = TestSubscriber<Int>()

    value
        .observable
        .subscribe(testSubscriber)
    value.value = NEXT_VALUE

    testSubscriber.assertValue(NEXT_VALUE)
  }

  @Test
  fun observable_setValue_canSetDescendantType() {
    val value = ObservableValue<Number>(false, INITIAL_VALUE)
    val testSubscriber = TestSubscriber<Number>()

    value
        .observable
        .subscribe(testSubscriber)
    value.value = NEXT_DOUBLE_VALUE

    testSubscriber.assertValue(NEXT_DOUBLE_VALUE)
  }

}