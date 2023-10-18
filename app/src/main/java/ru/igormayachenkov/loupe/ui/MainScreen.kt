@file:OptIn(ExperimentalPermissionsApi::class)

package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun MainScreen(){
    val viewModel : MainViewModel = viewModel()
    val image: Bitmap? by viewModel.imageFlow.collectAsState()
    
    image?.let {
        Text(text = "image ${it.width}x${it.height}")
    }?:run{
        val cameraPermissionState: PermissionState =
            rememberPermissionState(android.Manifest.permission.CAMERA)

        if (cameraPermissionState.status.isGranted) {
            CameraScreen(onPhotoTaken = viewModel::setImage)
        } else {
            Column {
                Text(text = "The camera permissions are not granted")
                Button(onClick = cameraPermissionState::launchPermissionRequest) {
                    Text(text = "Grant the permissions")
                }
            }
        }
    }

}