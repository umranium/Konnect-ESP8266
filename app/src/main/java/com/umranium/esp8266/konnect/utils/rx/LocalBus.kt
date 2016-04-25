package com.umranium.esp8266.konnect.utils.rx

import rx.subjects.PublishSubject

/**
 * A bus for delivering and waiting for local messages.
 */
class LocalBus {

  private val subject = PublishSubject.create<BaseLocalMessage>()

  fun <T : BaseLocalMessage> wait(clazz: Class<T>) = subject.asObservable().ofType(clazz)

  fun <T : BaseLocalMessage> emit(t: T) = subject.onNext(t)

}
