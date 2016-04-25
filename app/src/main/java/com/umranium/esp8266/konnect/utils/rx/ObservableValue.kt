package com.umranium.esp8266.konnect.utils.rx

import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subjects.Subject

/**
 * An observable value. Emits an event whenever the value is set.

 * @author umran
 */
class ObservableValue<T> {

  private val subject: Subject<T, T>

  var value: T
    @Synchronized set(value) {
      subject.onNext(value)
    }

  val observable: Observable<T>
    get() = subject.asObservable()

  constructor(onSubscribeEmitLast: Boolean, initialValue: T) {
    if (onSubscribeEmitLast) {
      subject = BehaviorSubject.create(initialValue)
    } else {
      subject = PublishSubject.create<T>()
    }
    value = initialValue
  }

}
