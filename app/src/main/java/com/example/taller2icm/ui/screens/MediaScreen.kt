package com.example.taller2icm.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(onBack: () -> Unit) {
    var isPhotoMode by remember { mutableStateOf(true) }
    var mediaUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cámara y Galería") },
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
            // Switch Foto/Video
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Video")
                Switch(
                    checked = isPhotoMode,
                    onCheckedChange = { isPhotoMode = it }
                )
                Text("Foto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* Tomar foto/video */ }) {
                    Text(if (isPhotoMode) "Tomar Foto" else "Grabar Video")
                }
                Button(onClick = { /* Seleccionar desde galería */ }) {
                    Text(if (isPhotoMode) "Seleccionar Foto" else "Seleccionar Video")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Área de visualización
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // TODO: Mostrar imagen o video aquí
                Text("Contenido multimedia aparecerá aquí")
            }
        }
    }
}
