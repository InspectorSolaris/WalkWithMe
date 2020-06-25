package com.example.walkwithme.presenter.map

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.walkwithme.MapViewInterface
import com.example.walkwithme.R
import com.example.walkwithme.model.Algorithms
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.location.POI
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.collections.ArrayList

class MapPresenter(
    private val mapInterface: MapViewInterface
) {
    private val wayPoints = ArrayList<GeoPoint>()
    private var lastRoad: Polyline? = null

    fun buildRoute() {
        mapInterface.mapRemoveOverlay(lastRoad)

        val roadManager = MapQuestRoadManager("sudOFI4elaABURi9uNTp74tdaN3scVcb").also {
            it.addRequestOption("routeType=pedestrian")
        }
        val road = roadManager.getRoad(wayPoints)
        val filteredRoute = ArrayList<GeoPoint>()

        for (i in 0 until road.mRouteHigh.size step road.mLength.toInt()) {
            filteredRoute.add(road.mRouteHigh[i])
        }

        val poiProvider = NominatimPOIProvider("OSMBonusPackTutoUserAgent")
        val pointsOfInterest = ArrayList<POI>()

        for (i in 0 until filteredRoute.size) {
            pointsOfInterest.addAll(
                poiProvider.getPOICloseTo(
                    filteredRoute[i],
                    "cafe",
                    1,
                    road.mLength * 0.001
                )
            )
        }

        for (i in 0 until pointsOfInterest.size) {
            val curPointOfInterest = pointsOfInterest[i].mLocation

            if (curPointOfInterest !in wayPoints) {
                wayPoints.add(1, curPointOfInterest)
            }
        }

        val distance = Array(wayPoints.size) { Array(wayPoints.size) { 0.0 } }

        for (i in 0 until wayPoints.size - 1) {
            for (j in i + 1 until wayPoints.size) {
                val roadFragment = roadManager.getRoad(
                    arrayListOf(
                        wayPoints[i],
                        wayPoints[j]
                    )
                )

                distance[i][j] = roadFragment.mLength
                distance[j][i] = roadFragment.mLength
            }
        }

        val path = Algorithms.runGenetic(distance, 10.0)
        val newWayPoints = ArrayList<GeoPoint>()

        for (i in path) {
            newWayPoints.add(wayPoints[i])

            if (i != 0 && i != wayPoints.size - 1) {
                setMarker(
                    createPOIMarker(
                        wayPoints[i].latitude,
                        wayPoints[i].longitude,
                        path.indexOf(i)
                    )
                )
            }
        }

        lastRoad = RoadManager.buildRoadOverlay(
            roadManager.getRoad(newWayPoints)
        )
        mapInterface.mapAddOverlay(lastRoad)
        mapInterface.mapInvalidate()
    }

    fun setOnMapTapListener() {
        val mReceiver = object: MapEventsReceiver {

            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                if (wayPoints.size < 2) {
                    setMarker(
                        createPathMarker(
                            p.latitude,
                            p.longitude,
                            wayPoints.size
                        )
                    )
                    wayPoints.add(p)
                }

                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }

        }

        mapInterface.mapAddOverlay(
            MapEventsOverlay(mReceiver)
        )
    }

    fun setRotationGestureOverlay() {
        val mRotationGestureOverlay = mapInterface.getRotationGestureOverlay().also {
            it.isEnabled = true
        }

        mapInterface.setMapMultiTouchControls(true)
        mapInterface.mapAddOverlay(mRotationGestureOverlay)
    }

    fun setMyLocationOverlay() {
        val mMyLocationOverlay = mapInterface.getMyLocationOverlay().also {
            it.disableMyLocation()
            it.disableFollowLocation()
            it.isDrawAccuracyEnabled = true
        }

        mapInterface.mapAddOverlay(mMyLocationOverlay)
    }

    fun setDefaultRotation() {
        mapInterface.getMap().controller.animateTo(
            mapInterface.getMap().mapCenter,
            mapInterface.getMap().zoomLevelDouble,
            1000,
            0f
        )
    }

    private fun setMarker(
        marker: Marker
    ) {
        mapInterface.mapAddOverlay(marker)
        mapInterface.mapInvalidate()
    }

    private fun createMarker(
        latitude: Double,
        longitude: Double
    ): Marker {
        return mapInterface.getMarker().also {
            it.position = GeoPoint(latitude, longitude)
            it.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
    }

    private fun createPathMarker(
        latitude: Double,
        longitude: Double,
        index: Int
    ): Marker {
        return createMarker(latitude, longitude).also {
            it.isDraggable = true
            it.icon = ContextCompat.getDrawable(
                mapInterface as Context,
                R.drawable.marker
            )
            when (index) {
                0 -> {
                    it.title = "Start"
                }
                1 -> {
                    it.title = "Finish"
                }
            }
            it.setOnMarkerDragListener(
                object : Marker.OnMarkerDragListener {

                    override fun onMarkerDrag(marker: Marker) { }
                    override fun onMarkerDragStart(marker: Marker) { }
                    override fun onMarkerDragEnd(marker: Marker) {
                        wayPoints[index] = marker.position
                    }

                }
            )
        }
    }

    private fun createPOIMarker(
        latitude: Double,
        longitude: Double,
        index: Int
    ): Marker {
        return createMarker(latitude, longitude).also {
            it.icon = ContextCompat.getDrawable(
                mapInterface as Context,
                R.drawable.marker_poi
            )
            it.title = index.toString()
        }
    }
}