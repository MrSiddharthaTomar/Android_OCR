@file:OptIn(ExperimentalPermissionsApi::class)
package com.example.ocr

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun Screen(){
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest
    )
}

@Composable
private fun MainContent(
    hasPermission : Boolean,
    onRequestPermission: () ->Unit)
{
    if(hasPermission){
        CameraScreen()
    }
    else{
        NoPermissionScreen(onRequestPermission)
    }

}
