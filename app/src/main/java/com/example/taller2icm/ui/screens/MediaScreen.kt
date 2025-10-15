package com.example.taller2icm.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.example.taller2icm.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MediaScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    // Estados
    var isPhotoMode by remember { mutableStateOf(true) }
    var mediaUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var currentVideoUri by remember { mutableStateOf<Uri?>(null) }

    // Permisos de cámara
    val cameraPermission = rememberCameraPermission()

    // Permisos de video (cámara + audio)
    val videoPermissions = rememberVideoPermissions()

    // Launchers para cámara
    val (takePhotoLauncher, createPhotoUri) = rememberTakePhotoLauncher(context) { uri ->
        mediaUri = uri
        currentPhotoUri = uri
    }

    val (takeVideoLauncher, createVideoUri) = rememberTakeVideoLauncher(context) { uri ->
        mediaUri = uri
        currentVideoUri = uri
    }

    // Launchers para galería
    val pickImageLauncher = rememberPickImageLauncher { uri ->
        uri?.let {
            mediaUri = it
            currentPhotoUri = it
        }
    }

    val pickVideoLauncher = rememberPickVideoLauncher { uri ->
        uri?.let {
            mediaUri = it
            currentVideoUri = it
        }
    }

    // Funciones para manejar los botones
    fun handleTakePhoto() {
        if (cameraPermission.status.isGranted) {
            val uri = createPhotoUri()
            currentPhotoUri = uri
            takePhotoLauncher.launch(uri)
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    fun handleTakeVideo() {
        if (videoPermissions.allPermissionsGranted) {
            val uri = createVideoUri()
            currentVideoUri = uri
            takeVideoLauncher.launch(uri)
        } else {
            videoPermissions.launchMultiplePermissionRequest()
        }
    }

    fun handleSelectPhoto() {
        pickImageLauncher.launch("image/*")
    }

    fun handleSelectVideo() {
        pickVideoLauncher.launch("video/*")
    }

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
                Text(
                    "Video",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (!isPhotoMode) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isPhotoMode,
                    onCheckedChange = {
                        isPhotoMode = it
                        // Actualizar mediaUri según el modo
                        mediaUri = if (it) currentPhotoUri else currentVideoUri
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Foto",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isPhotoMode) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (isPhotoMode) handleTakePhoto()
                        else handleTakeVideo()
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.CameraAlt,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (isPhotoMode) "Tomar Foto" else "Grabar Video")
                    }
                }

                Button(
                    onClick = {
                        if (isPhotoMode) handleSelectPhoto()
                        else handleSelectVideo()
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.PhotoLibrary,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (isPhotoMode) "Seleccionar Foto" else "Seleccionar Video")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Área de visualización
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        mediaUri != null && isPhotoMode && currentPhotoUri != null -> {
                            // Mostrar imagen
                            Image(
                                painter = rememberAsyncImagePainter(mediaUri),
                                contentDescription = "Foto capturada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        mediaUri != null && !isPhotoMode && currentVideoUri != null -> {
                            // Mostrar video
                            VideoPlayer(videoUri = mediaUri!!)
                        }
                        else -> {
                            // Placeholder
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isPhotoMode)
                                        androidx.compose.material.icons.Icons.Default.Image
                                    else
                                        androidx.compose.material.icons.Icons.Default.VideoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (isPhotoMode)
                                        "La foto aparecerá aquí"
                                    else
                                        "El video aparecerá aquí",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(videoUri: Uri) {
    val context = LocalContext.current

    // Crear ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
    }

    // Limpiar el player cuando el composable se destruya
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Vista del reproductor
    AndroidView(
        factory = { ctx ->
            StyledPlayerView(ctx).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}