package com.umranium.esp8266.konnect.utils.rx

import rx.Observable

/**
 * Maps any value given to a single value provided at initialisation.

 * @author umran
 */
fun <T, R> Observable<T>.toValue(_this: Observable<T>, value: R): Observable<R> {
  return _this.map({ return@map value })
}
