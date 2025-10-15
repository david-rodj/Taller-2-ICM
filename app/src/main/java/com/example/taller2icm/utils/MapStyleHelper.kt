package com.example.taller2icm.utils

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

object MapStyleHelper {
    fun applyLightStyle(mapView: MapView) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
    }

    fun applyDarkStyle(mapView: MapView) {
        // Usar un tile source oscuro como CartoDB Dark Matter
        // o configurar un overlay oscuro
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        // TODO: Implementar estilo oscuro personalizado
    }
}