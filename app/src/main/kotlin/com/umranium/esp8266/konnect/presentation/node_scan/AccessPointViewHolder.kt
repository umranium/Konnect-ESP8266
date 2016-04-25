package com.umranium.esp8266.konnect.presentation.node_scan

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.umranium.esp8266.konnect.R
import rx.Observer

/**
 * A row that displays an access point.
 */
class AccessPointViewHolder(itemView: View, private val clickObserver: Observer<ScannedAccessPoint>) :
    RecyclerView.ViewHolder(itemView) {

  private val name: TextView
  private val sigStrength: TextView
  private var boundAccessPoint: ScannedAccessPoint? = null

  init {
    name = itemView.findViewById(R.id.txt_name) as TextView
    sigStrength = itemView.findViewById(R.id.txt_sig_strength) as TextView

    itemView.setOnClickListener { clickObserver.onNext(boundAccessPoint) }
  }

  fun bindTo(accessPoint: ScannedAccessPoint) {
    name.text = accessPoint.ssid
    sigStrength.text = String.format("%d%%", accessPoint.signalStrength)
    itemView.isSelected = true
    this.boundAccessPoint = accessPoint
  }

}
