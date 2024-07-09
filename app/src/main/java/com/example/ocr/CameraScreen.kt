package com.example.ocr

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(){
    CameraContent()
}

@Composable
private fun CameraContent() {
    var ans by remember { mutableStateOf("Extracted text will be displayed here") }
    var showCamXCamera by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

    if (showCamXCamera) {
        camXCamera(onImageCaptured = { extractedText ->
            ans = extractedText
            showCamXCamera = false // Hide the camera preview
        })
    } else {
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = ans,
                    color = Color.White,
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(Color.Cyan),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Row(Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(119.dp)
                    ) {
                        Image(painter = painterResource(id = R.drawable.eraser), contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    ans = ""
                                })
                        Image(painter = painterResource(id = R.drawable.icons8_camera_50), contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    showCamXCamera = true
                                })
                        Image(painter = painterResource(id = R.drawable.icons8_copy_24), contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    clipboardManager.setPrimaryClip(
                                        ClipData.newPlainText(
                                            "Extracted Text",
                                            ans.toString()
                                        )
                                    )
                                    Toast
                                        .makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT)
                                        .show()
                                })
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(130.dp)) {
                        Text(text = "Erase", color = Color.Black)
                        Text(text = "Camera", color = Color.Black)
                        Text(text = "Copy", color = Color.Black)
                    }
                }
            }
        }
    }
}
