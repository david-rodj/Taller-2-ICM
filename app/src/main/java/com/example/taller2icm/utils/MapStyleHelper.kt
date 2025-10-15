package com.example.taller2icm.utils

import android.util.Log
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView

object MapStyleHelper {

    // Tile source para modo claro (Mapnik - estándar de OpenStreetMap)
    fun applyLightStyle(mapView: MapView) {
        Log.d("MapStyleHelper", "Aplicando estilo CLARO (Mapnik)")
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.invalidate() // Forzar redibujado
        Log.d("MapStyleHelper", "Estilo claro aplicado correctamente")
    }

    // Tile source para modo oscuro (CartoDB Dark Matter)
    fun applyDarkStyle(mapView: MapView) {
        Log.d("MapStyleHelper", "Aplicando estilo OSCURO (CartoDB Dark)")

        val darkTileSource = object : OnlineTileSourceBase(
            "CartoDark",
            0, 20, 256, ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/dark_all/",
                "https://b.basemaps.cartocdn.com/dark_all/",
                "https://c.basemaps.cartocdn.com/dark_all/",
                "https://d.basemaps.cartocdn.com/dark_all/"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return baseUrl +
                        MapTileIndex.getZoom(pMapTileIndex) + "/" +
                        MapTileIndex.getX(pMapTileIndex) + "/" +
                        MapTileIndex.getY(pMapTileIndex) +
                        mImageFilenameEnding
            }
        }

        mapView.setTileSource(darkTileSource)
        mapView.invalidate() // Forzar redibujado
        Log.d("MapStyleHelper", "Estilo oscuro aplicado correctamente")
    }
}