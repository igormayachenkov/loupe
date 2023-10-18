@file:OptIn(ExperimentalPermissionsApi::class)

package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.ui.unit.dp


@Composable
fun MainScreen(){
    val viewModel : MainViewModel = viewModel()
    val image: Bitmap? by viewModel.imageFlow.collectAsState()
    
    image?.let {image->
        val imageBitmap: ImageBitmap = remember(image.hashCode()) { image.asImageBitmap() }
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = imageBitmap,
            contentDescription = "Last captured photo",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "image ${imageBitmap.width}x${imageBitmap.height}")
            Button(onClick = viewModel::clearImage) {
                Text("Clear")
            }
        }
    }?:run{
        val cameraPermissionState: PermissionState =
            rememberPermissionState(android.Manifest.permission.CAMERA)

        if (cameraPermissionState.status.isGranted) {
            // Camera Preview Screen
            CameraScreen(onPhotoTaken = viewModel::setImage)
        } else {
            // No Permissions Screen
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "The camera permissions are not granted")
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = cameraPermissionState::launchPermissionRequest) {
                    Text(text = "Grant the permissions")
                }
            }
        }
    }

}