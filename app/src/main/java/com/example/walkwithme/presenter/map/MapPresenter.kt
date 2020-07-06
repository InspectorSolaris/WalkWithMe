package com.example.walkwithme.presenter.map

import androidx.core.content.ContextCompat
import com.example.walkwithme.view.map.MapViewInterface
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
import kotlin.collections.ArrayList
import kotlin.math.max

class MapPresenter(
    private val mapInterface: MapViewInterface
) {
    fun requestRoute() {

    }

    fun buildRoute() {
        for (marker in mapInterface.poiMarkers) {
            mapInterface.getMap().overlays.remove(marker)
        }
        mapInterface.mapRemoveOverlay(mapInterface.lastRoad)

        val roadManager = MapQuestRoadManager("sudOFI4elaABURi9uNTp74tdaN3scVcb").also {
            it.addRequestOption("routeType=pedestrian")
        }
        val road = roadManager.getRoad(mapInterface.wayPoints)
        val filteredRoute = ArrayList<GeoPoint>()

        for (i in 0 until road.mRouteHigh.size step max(1, road.mLength.toInt())) {
            filteredRoute.add(road.mRouteHigh[i])
        }

        val poiProvider = NominatimPOIProvider("OSMBonusPackTutoUserAgent")
        val pointsOfInterest = ArrayList<POI>()

        for (i in 0 until filteredRoute.size) {
            try {
                pointsOfInterest.addAll(
                    poiProvider.getPOICloseTo(
                        filteredRoute[i],
                        "Monument",
                        1,
                        road.mLength * 0.001
                    )
                )
            } catch (e: Exception) {
            }
        }

        for (i in 0 until pointsOfInterest.size) {
            val curPointOfInterest = pointsOfInterest[i].mLocation

            if (curPointOfInterest !in mapInterface.wayPoints) {
                mapInterface.wayPoints.add(1, curPointOfInterest)
            }
        }

        val distance =
            Array(mapInterface.wayPoints.size) { Array(mapInterface.wayPoints.size) { 0.0 } }

        for (i in 0 until mapInterface.wayPoints.size - 1) {
            for (j in i + 1 until mapInterface.wayPoints.size) {
                val roadFragment = roadManager.getRoad(
                    arrayListOf(
                        mapInterface.wayPoints[i],
                        mapInterface.wayPoints[j]
                    )
                )

                distance[i][j] = roadFragment.mLength
                distance[j][i] = roadFragment.mLength
            }
        }

        val path = Algorithms.runGenetic(distance, road.mLength * 1.2)
        val newWayPoints = ArrayList<GeoPoint>()

        for (i in path) {
            newWayPoints.add(mapInterface.wayPoints[i])

            if (i != 0 && i != mapInterface.wayPoints.size - 1) {
                setMarker(
                    createPOIMarker(
                        mapInterface.wayPoints[i].latitude,
                        mapInterface.wayPoints[i].longitude,
                        path.indexOf(i)
                    )
                )
            }
        }

        mapInterface.wayPoints.subList(1, mapInterface.wayPoints.size - 1).clear()
        mapInterface.lastRoad = RoadManager.buildRoadOverlay(
            roadManager.getRoad(newWayPoints),
            0x7f338a3e,
            7.5f
        )
        mapInterface.mapAddOverlay(mapInterface.lastRoad)
        mapInterface.mapInvalidate()
    }

    fun setOnMapTapListener() {
        val mReceiver = object : MapEventsReceiver {

            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                if (mapInterface.wayPoints.size < 2) {
                    setMarker(
                        createPathMarker(
                            p.latitude,
                            p.longitude,
                            mapInterface.wayPoints.size
                        )
                    )
                    mapInterface.wayPoints.add(p)
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
                mapInterface.getMap().context,
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

                    override fun onMarkerDrag(marker: Marker) {}
                    override fun onMarkerDragStart(marker: Marker) {}
                    override fun onMarkerDragEnd(marker: Marker) {
                        mapInterface.wayPoints[index] = marker.position
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
                mapInterface.getMap().context,
                R.drawable.marker_poi
            )
            it.title = index.toString()
            mapInterface.poiMarkers.add(it)
        }
    }
}