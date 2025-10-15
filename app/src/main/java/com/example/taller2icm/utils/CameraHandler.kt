package com.example.taller2icm.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun rememberTakePhotoLauncher(
    context: Context,
    onPhotoTaken: (Uri) -> Unit
): Pair<ManagedActivityResultLauncher<Uri, Boolean>, () -> Uri> {
    var latestUri: Uri? = null

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && latestUri != null) {
            onPhotoTaken(latestUri!!)
        }
    }

    val createUri: () -> Uri = {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = File(context.getExternalFilesDir(null), "Pictures")
        storageDir.mkdirs()
        val photoFile = File(storageDir, "PHOTO_$timeStamp.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
        latestUri = uri
        uri
    }

    return Pair(launcher, createUri)
}

@Composable
fun rememberTakeVideoLauncher(
    context: Context,
    onVideoRecorded: (Uri) -> Unit
): Pair<ManagedActivityResultLauncher<Uri, Boolean>, () -> Uri> {
    var latestUri: Uri? = null

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && latestUri != null) {
            onVideoRecorded(latestUri!!)
        }
    }

    val createUri: () -> Uri = {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = File(context.getExternalFilesDir(null), "Movies")
        storageDir.mkdirs()
        val videoFile = File(storageDir, "VIDEO_$timeStamp.mp4")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", videoFile)
        latestUri = uri
        uri
    }

    return Pair(launcher, createUri)
}

@Composable
fun rememberPickImageLauncher(
    onImagePicked: (Uri?) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImagePicked(uri)
    }
}

@Composable
fun rememberPickVideoLauncher(
    onVideoPicked: (Uri?) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onVideoPicked(uri)
    }
}