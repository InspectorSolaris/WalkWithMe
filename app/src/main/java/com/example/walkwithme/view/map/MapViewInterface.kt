package com.example.walkwithme.view.map

import android.graphics.drawable.Drawable
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

interface MapViewInterface {

    fun getMap(): MapView
    fun getMarker(): Marker
    fun getPathMarkerIcon(): Drawable?
    fun getPOIMarkerIcon(): Drawable?
    fun getRotationGestureOverlay(): RotationGestureOverlay
    fun getMyLocationOverlay(): MyLocationNewOverlay
    fun setMapMultiTouchControls(on: Boolean)
    fun mapInvalidate()
    fun mapAddOverlay(overlay: Overlay?)
    fun mapRemoveOverlay(overlay: Overlay?)

}