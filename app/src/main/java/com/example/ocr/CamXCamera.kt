package com.example.ocr

import android.content.Context
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
){
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(factory = {
        PreviewView(it).apply{
            this.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
        }
    },
        modifier = modifier)

}

@Composable
fun camXCamera(onImageCaptured: (String) -> Unit) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())
        Image(painter = painterResource(id = R.drawable.camshutter), contentDescription = "Take Photo",
            Modifier
                .size(70.dp)
                .align(Alignment.BottomCenter)
                .padding(10.dp)
                .clickable {
                    takePhoto(controller, context, onImageCaptured)
                })
    }
}

fun takePhoto(controller: LifecycleCameraController, context: Context, onImageCaptured: (String) -> Unit) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                analyze(image) { extractedText ->
                    onImageCaptured(extractedText)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo", exception)
                onImageCaptured("")
            }
        },
    )
}


@OptIn(ExperimentalGetImage::class)
private fun analyze(imageProxy: ImageProxy, onTextExtracted: (String) -> Unit) {
    val textRecognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = imageProxy.image
    if (image != null) {
        val mediaImage: Image = image
        val inputImage: InputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText: Text ->
                val extractedText = visionText.text
                onTextExtracted(extractedText)
            }
            .addOnFailureListener { e ->
                Log.e("Analyze", "Text recognition failed", e)
                onTextExtracted("")
            }
    } else {
        Log.d("Camera", "Image null, Error occurred")
        onTextExtracted("")
    }
    imageProxy.close()
}