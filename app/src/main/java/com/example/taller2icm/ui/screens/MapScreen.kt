package com.example.taller2icm.ui.screens

import android.content.Context
import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.taller2icm.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polyline

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Estados
    var searchText by remember { mutableStateOf("") }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var currentLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var pathPoints by remember { mutableStateOf<MutableList<GeoPoint>>(mutableListOf()) }
    var polyline by remember { mutableStateOf<Polyline?>(null) }
    var isDarkMode by remember { mutableStateOf(false) }

    // Helpers
    val locationHandler = remember { LocationHandler(context) }
    val geocoderHelper = remember { GeocoderHelper(context) }
    val lightSensorHandler = remember { LightSensorHandler(context) }

    // Permisos de ubicación
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Inicialización y limpieza
    DisposableEffect(Unit) {
        // Solicitar permisos al entrar
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        }

        // Iniciar sensor de luz
        lightSensorHandler.startListening { isDark ->
            isDarkMode = isDark
            mapView?.let { map ->
                if (isDark) {
                    MapStyleHelper.applyDarkStyle(map)
                } else {
                    MapStyleHelper.applyLightStyle(map)
                }
            }
        }

        onDispose {
            lightSensorHandler.stopListening()
            locationHandler.stopLocationUpdates()
        }
    }

    // Efecto para iniciar seguimiento de ubicación cuando se otorgan permisos
    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            // Obtener ubicación actual inicial
            locationHandler.getCurrentLocation { geoPoint ->
                currentLocation = geoPoint
                mapView?.let { map ->
                    MapHelper.centerMapOnLocation(map, geoPoint, 15.0)
                    MapHelper.addMarker(map, geoPoint, "Mi Ubicación")
                }
            }

            // Iniciar actualizaciones de ubicación
            locationHandler.startLocationUpdates { geoPoint ->
                currentLocation = geoPoint

                // Agregar punto al polyline
                pathPoints.add(geoPoint)

                mapView?.let { map ->
                    if (polyline == null) {
                        // Crear polyline si no existe
                        polyline = MapHelper.addPolyline(map, pathPoints)
                    } else {
                        // Actualizar polyline existente
                        MapHelper.updatePolyline(polyline!!, pathPoints, map)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // TextField para búsqueda de direcciones
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar dirección") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchText.isNotEmpty()) {
                                scope.launch {
                                    val location = geocoderHelper.getLocationFromAddress(searchText)
                                    location?.let { geoPoint ->
                                        mapView?.let { map ->
                                            // Agregar marcador en la ubicación encontrada
                                            MapHelper.addMarker(map, geoPoint, searchText)
                                            // Mover cámara a la ubicación
                                            MapHelper.centerMapOnLocation(map, geoPoint, 15.0)
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    }
                }
            )

            // Indicador de modo del mapa
            Text(
                text = "Modo: ${if (isDarkMode) "Oscuro" else "Claro"}",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mapa OpenStreetMap
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { ctx ->
                        createMapView(ctx, geocoderHelper, scope).also {
                            mapView = it
                            // Aplicar estilo inicial
                            if (isDarkMode) {
                                MapStyleHelper.applyDarkStyle(it)
                            } else {
                                MapStyleHelper.applyLightStyle(it)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Mostrar diálogo si no hay permisos
    if (!locationPermissions.allPermissionsGranted) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Permisos necesarios") },
            text = { Text("Se necesitan permisos de ubicación para mostrar el mapa correctamente.") },
            confirmButton = {
                Button(onClick = { locationPermissions.launchMultiplePermissionRequest() }) {
                    Text("Otorgar permisos")
                }
            },
            dismissButton = {
                Button(onClick = onBack) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun createMapView(
    context: Context,
    geocoderHelper: GeocoderHelper,
    scope: kotlinx.coroutines.CoroutineScope
): MapView {
    return MapView(context).apply {
        setMultiTouchControls(true)
        controller.setZoom(15.0)

        // Ubicación inicial por defecto (Bogotá, Colombia)
        val defaultLocation = GeoPoint(4.6097, -74.0817)
        controller.setCenter(defaultLocation)

        // Agregar overlay para eventos del mapa (long click)
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let { geoPoint ->
                    // Obtener dirección del punto (reverse geocoding)
                    scope.launch {
                        val address = geocoderHelper.getAddressFromLocation(geoPoint)
                        val markerTitle = address ?: "Lat: ${geoPoint.latitude}, Lng: ${geoPoint.longitude}"

                        // Agregar marcador con la dirección como título
                        MapHelper.addMarker(this@apply, geoPoint, markerTitle)
                    }
                }
                return true
            }
        })

        overlays.add(mapEventsOverlay)
    }
}