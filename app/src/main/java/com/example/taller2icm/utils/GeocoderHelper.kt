package com.example.taller2icm.utils

import android.content.Context
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import java.io.IOException

class GeocoderHelper(private val context: Context) {
    private val geocoder = Geocoder(context)

    suspend fun getAddressFromLocation(geoPoint: GeoPoint): String? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // API 33+
                var result: String? = null
                geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1) { addresses ->
                    result = addresses.firstOrNull()?.getAddressLine(0)
                }
                result
            } else {
                // API anterior
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
                addresses?.firstOrNull()?.getAddressLine(0)
            }
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getLocationFromAddress(address: String): GeoPoint? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // API 33+
                var result: GeoPoint? = null
                geocoder.getFromLocationName(address, 1) { addresses ->
                    addresses.firstOrNull()?.let {
                        result = GeoPoint(it.latitude, it.longitude)
                    }
                }
                result
            } else {
                // API anterior
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(address, 1)
                addresses?.firstOrNull()?.let {
                    GeoPoint(it.latitude, it.longitude)
                }
            }
        } catch (e: IOException) {
            null
        }
    }
}