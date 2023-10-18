@file:OptIn(ExperimentalPermissionsApi::class)

package ru.igormayachenkov.loupe.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun MainScreen(){
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if(cameraPermissionState.status.isGranted){
        Text(text = "granted")
    }else{
        Column {
            Text(text = "The camera permissions are not granted")
            Button(onClick = cameraPermissionState::launchPermissionRequest) {
                Text(text = "Grant the permissions")
            }
        }
    }

}