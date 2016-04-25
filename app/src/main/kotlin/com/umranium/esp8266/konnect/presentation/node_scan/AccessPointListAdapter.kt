package com.umranium.esp8266.konnect.presentation.node_scan

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.umranium.esp8266.konnect.R
import rx.Observable
import rx.subjects.PublishSubject
import java.util.*

/**
 * Adapter for the access-point list.
 */
class AccessPointListAdapter : RecyclerView.Adapter<AccessPointViewHolder>() {

  private val accessPoints = ArrayList<ScannedAccessPoint>()
  private val comparator = Comparator<ScannedAccessPoint> { lhs, rhs ->
    lhs.ssid.toLowerCase().compareTo(rhs.ssid.toLowerCase())
  }

  private val _clickEvents = PublishSubject.create<ScannedAccessPoint>()

  val clickEvents: Observable<ScannedAccessPoint>
    get() = _clickEvents.asObservable()

  init {
    this.setHasStableIds(true)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccessPointViewHolder {
    val itemView = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.layout_scanned_access_point, parent, false)
    return AccessPointViewHolder(itemView, _clickEvents)
  }

  override fun getItemCount(): Int {
    return accessPoints.size
  }

  override fun getItemId(position: Int): Long {
    return accessPoints[position].id
  }

  override fun onBindViewHolder(holder: AccessPointViewHolder, position: Int) {
    holder.bindTo(accessPoints[position])
  }

  fun populate(value: Collection<ScannedAccessPoint>) {
    accessPoints.clear()
    accessPoints.addAll(value)
    Collections.sort(accessPoints, comparator)
    notifyDataSetChanged()
  }

}
