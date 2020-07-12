package com.example.walkwithme.model.infowindow

import android.view.View
import androidx.cardview.widget.CardView
import com.example.walkwithme.R
import kotlinx.android.synthetic.main.marker_info_window.view.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow


class MarkerInfoWindow(layoutResId: Int, mapView: MapView?) : InfoWindow(layoutResId, mapView) {

    override fun onOpen(item: Any) {
        val marker = item as Marker
        val layout = mView.findViewById<CardView>(R.id.InfoWindowCardView)
        layout.Number.text = marker.title
        layout.Category.text = marker.snippet
        layout.Description.text = marker.subDescription
        layout.setOnClickListener { closeAllInfoWindowsOn(mapView) }
    }

    override fun onClose() {}

}