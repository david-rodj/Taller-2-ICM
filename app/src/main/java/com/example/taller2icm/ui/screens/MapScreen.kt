package com.example.taller2icm.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.taller2icm.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
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
    var userMarker by remember { mutableStateOf<Marker?>(null) }
    var pathPoints by remember { mutableStateOf<MutableList<GeoPoint>>(mutableListOf()) }
    var polyline by remember { mutableStateOf<Polyline?>(null) }
    var isDarkMode by remember { mutableStateOf(false) }
    var followUser by remember { mutableStateOf(true) }

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
            Log.d("MapScreen", "Sensor de luz cambió. ¿Es oscuro?: $isDark. Lux detectado")
            isDarkMode = isDark
            mapView?.let { map ->
                Log.d("MapScreen", "Aplicando estilo ${if (isDark) "OSCURO" else "CLARO"}")
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
                    // Crear o actualizar marcador del usuario
                    if (userMarker == null) {
                        userMarker = MapHelper.addMarker(map, geoPoint, "Mi Ubicación", "Ubicación actual")
                    } else {
                        userMarker?.position = geoPoint
                        map.invalidate()
                    }

                    if (followUser) {
                        MapHelper.centerMapOnLocation(map, geoPoint, 15.0)
                    }
                }
            }

            // Iniciar actualizaciones de ubicación
            locationHandler.startLocationUpdates { geoPoint ->
                currentLocation = geoPoint

                // Actualizar marcador del usuario
                mapView?.let { map ->
                    if (userMarker == null) {
                        userMarker = MapHelper.addMarker(map, geoPoint, "Mi Ubicación", "Ubicación actual")
                    } else {
                        userMarker?.position = geoPoint
                        map.invalidate()
                    }

                    // Centrar mapa si followUser está activo
                    if (followUser) {
                        MapHelper.centerMapOnLocation(map, geoPoint, 15.0)
                    }
                }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // TextField para búsqueda de direcciones
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar dirección") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchText.isNotEmpty()) {
                                scope.launch {
                                    Log.d("MapScreen", "Buscando: $searchText")
                                    val location = geocoderHelper.getLocationFromAddress(searchText)
                                    if (location != null) {
                                        Log.d("MapScreen", "Ubicación encontrada: ${location.latitude}, ${location.longitude}")
                                        mapView?.let { map ->
                                            // Agregar marcador en la ubicación encontrada
                                            MapHelper.addMarker(map, location, searchText)
                                            // Mover cámara a la ubicación
                                            MapHelper.centerMapOnLocation(map, location, 15.0)
                                        }
                                    } else {
                                        Log.d("MapScreen", "No se encontró la ubicación")
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle para seguir al usuario
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seguir mi ubicación",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = followUser,
                    onCheckedChange = {
                        followUser = it
                        if (it && currentLocation != null) {
                            mapView?.let { map ->
                                MapHelper.centerMapOnLocation(map, currentLocation!!, 15.0)
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Indicador de modo del mapa
            Text(
                text = "Modo: ${if (isDarkMode) "Oscuro 🌙" else "Claro ☀️"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contenedor del mapa con borde y padding
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .border(2.dp, MaterialTheme.colorScheme.outline)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            createMapView(ctx, geocoderHelper, scope).also {
                                mapView = it
                                // Aplicar estilo inicial
                                Log.d("MapScreen", "Creando mapa con estilo inicial: ${if (isDarkMode) "OSCURO" else "CLARO"}")
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
                    Log.d("MapScreen", "Long press en: ${geoPoint.latitude}, ${geoPoint.longitude}")
                    // Obtener dirección del punto (reverse geocoding)
                    scope.launch {
                        val address = geocoderHelper.getAddressFromLocation(geoPoint)
                        val markerTitle = address ?: "Lat: ${geoPoint.latitude}, Lng: ${geoPoint.longitude}"
                        Log.d("MapScreen", "Dirección encontrada: $markerTitle")

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