package com.example.walkwithme.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.walkwithme.R
import com.example.walkwithme.presenter.map.MapPresenter
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment(),
    MapViewInterface {

    private var mapPresenter: MapPresenter? = null

    override var wayPoints = ArrayList<GeoPoint>()
    override var poiMarkers = ArrayList<Marker>()
    override var lastRoad: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPresenter()
        setListeners()
        setMapConfiguration()
    }

    private fun setPresenter() {
        mapPresenter = MapPresenter(this).apply {
            setOnMapTapListener()
            setRotationGestureOverlay()
            setMyLocationOverlay()
        }
    }

    private fun setListeners() {
        BuildRouteButton.setOnClickListener { mapPresenter?.buildRoute() }
        MyLocationButton.setOnClickListener { mapPresenter?.setMyLocationOverlay() }
        CompassButton.setOnClickListener { mapPresenter?.setDefaultRotation() }
    }

    private fun setMapConfiguration() {
        Map.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(
                CustomZoomButtonsController.Visibility.NEVER
            )
            controller.setZoom(12.0)
            controller.setCenter(
                GeoPoint(
                    56.4977,
                    84.9744
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Map.onResume()
    }

    override fun onPause() {
        super.onPause()
        Map.onPause()
    }

    override fun getMap(): MapView {
        return Map
    }

    override fun getMarker(): Marker {
        return Marker(Map)
    }

    override fun getRotationGestureOverlay(): RotationGestureOverlay {
        return RotationGestureOverlay(Map)
    }

    override fun getMyLocationOverlay(): MyLocationNewOverlay {
        return MyLocationNewOverlay(
            GpsMyLocationProvider(requireContext()),
            Map
        )
    }

    override fun setMapMultiTouchControls(
        on: Boolean
    ) {
        Map.setMultiTouchControls(on)
    }

    override fun mapInvalidate() {
        Map.invalidate()
    }

    override fun mapAddOverlay(
        overlay: Overlay?
    ) {
        if (overlay != null) {
            Map.overlays.add(overlay)
        }
    }

    override fun mapRemoveOverlay(
        overlay: Overlay?
    ) {
        if (overlay != null) {
            Map.overlays.remove(overlay)
        }
    }
}