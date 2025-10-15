package com.example.taller2icm.utils

import android.graphics.Color
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

object MapHelper {

    fun addMarker(
        mapView: MapView,
        geoPoint: GeoPoint,
        title: String,
        snippet: String? = null
    ): Marker {
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.title = title
        marker.snippet = snippet
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
        mapView.invalidate()
        return marker
    }

    fun addPolyline(mapView: MapView, points: List<GeoPoint>): Polyline {
        val polyline = Polyline()
        polyline.setPoints(points)
        polyline.color = Color.BLUE
        polyline.width = 5f
        mapView.overlays.add(polyline)
        mapView.invalidate()
        return polyline
    }

    fun updatePolyline(polyline: Polyline, points: List<GeoPoint>, mapView: MapView) {
        polyline.setPoints(points)
        mapView.invalidate()
    }

    fun clearMarkers(mapView: MapView) {
        mapView.overlays.removeAll { it is Marker }
        mapView.invalidate()
    }

    fun centerMapOnLocation(mapView: MapView, geoPoint: GeoPoint, zoom: Double = 15.0) {
        mapView.controller.animateTo(geoPoint)
        mapView.controller.setZoom(zoom)
    }
}