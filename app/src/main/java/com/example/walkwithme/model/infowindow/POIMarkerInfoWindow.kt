package com.example.walkwithme.model.infowindow

import androidx.cardview.widget.CardView
import com.example.walkwithme.R
import kotlinx.android.synthetic.main.poi_marker_info.view.*
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class POIMarkerInfoWindow(layoutResId: Int, mapView: MapView?) : InfoWindow(layoutResId, mapView) {

    override fun onOpen(item: Any) {
        val marker = item as Marker
        val layout = mView.findViewById<CardView>(R.id.POIMarkerInfoCardView)
        layout.Number.text = marker.title
        layout.Category.text = marker.snippet
        layout.Description.text = marker.subDescription
        layout.setOnClickListener { closeAllInfoWindowsOn(mapView) }
    }

    override fun onClose() {}

}