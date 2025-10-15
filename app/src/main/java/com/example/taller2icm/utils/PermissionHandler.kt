package com.example.taller2icm.utils

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberCameraPermission(): PermissionState {
    return rememberPermissionState(Manifest.permission.CAMERA)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberLocationPermissions(): MultiplePermissionsState {
    return rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberVideoPermissions(): MultiplePermissionsState {
    return rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )
}