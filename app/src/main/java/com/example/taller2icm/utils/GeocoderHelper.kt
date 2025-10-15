package com.example.taller2icm.utils

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import java.io.IOException
import kotlin.coroutines.resume

class GeocoderHelper(private val context: Context) {
    private val geocoder = Geocoder(context)

    suspend fun getAddressFromLocation(geoPoint: GeoPoint): String? = withContext(Dispatchers.IO) {
        try {
            Log.d("GeocoderHelper", "Obteniendo dirección de: ${geoPoint.latitude}, ${geoPoint.longitude}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // API 33+
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1) { addresses ->
                        val result = addresses.firstOrNull()?.getAddressLine(0)
                        Log.d("GeocoderHelper", "Dirección encontrada (API 33+): $result")
                        continuation.resume(result)
                    }
                }
            } else {
                // API anterior
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
                val result = addresses?.firstOrNull()?.getAddressLine(0)
                Log.d("GeocoderHelper", "Dirección encontrada (API < 33): $result")
                result
            }
        } catch (e: IOException) {
            Log.e("GeocoderHelper", "Error obteniendo dirección: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("GeocoderHelper", "Error inesperado: ${e.message}")
            null
        }
    }

    suspend fun getLocationFromAddress(address: String): GeoPoint? = withContext(Dispatchers.IO) {
        try {
            Log.d("GeocoderHelper", "Buscando ubicación de: $address")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // API 33+
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocationName(address, 1) { addresses ->
                        val result = addresses.firstOrNull()?.let {
                            GeoPoint(it.latitude, it.longitude)
                        }
                        Log.d("GeocoderHelper", "Ubicación encontrada (API 33+): $result")
                        continuation.resume(result)
                    }
                }
            } else {
                // API anterior
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocationName(address, 1)
                val result = addresses?.firstOrNull()?.let {
                    GeoPoint(it.latitude, it.longitude)
                }
                Log.d("GeocoderHelper", "Ubicación encontrada (API < 33): $result")
                result
            }
        } catch (e: IOException) {
            Log.e("GeocoderHelper", "Error buscando ubicación: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("GeocoderHelper", "Error inesperado: ${e.message}")
            null
        }
    }
}