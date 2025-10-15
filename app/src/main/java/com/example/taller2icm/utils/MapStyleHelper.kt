package com.example.taller2icm.utils

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView

object MapStyleHelper {

    // Tile source para modo claro (Mapnik - estándar de OpenStreetMap)
    fun applyLightStyle(mapView: MapView) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
    }

    // Tile source para modo oscuro (CartoDB Dark Matter)
    fun applyDarkStyle(mapView: MapView) {
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
    }
}