package com.umranium.esp8266.konnect.utils.rx

import rx.Observable

/**
 * Maps any value given to a [Unit] value (effectively throwing away the value given).

 * @author umran
 */
fun <T> Observable<T>.toUnit(_this: Observable<T>): Observable<Unit> {
  return _this.map({})
}
