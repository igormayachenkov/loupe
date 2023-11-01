package ru.igormayachenkov.loupe.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import ru.igormayachenkov.loupe.rotateBitmap
import java.util.concurrent.Executor
import kotlin.math.roundToInt

private const val TAG = "myapp.CameraScreen"

private var screenSize: Size = Size(1000f,1000f)

@Composable
fun CameraScreen(
    onPhotoTaken:(Bitmap)->Unit
){

    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()){
            //Log.d(TAG,"screenSize $size ")
            screenSize = size
        }
        AndroidView(modifier = Modifier
            .fillMaxSize(),
            factory ={context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.BLACK)
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            capturePhoto(context, cameraController, onPhotoTaken)
        }) {
            Text(text = "Take photo")
        }
    }
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit
){
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            Log.d(TAG,"onCaptureSuccess image size:${image.width}x${image.width}  screenSize $screenSize")
            // image bitmap
            val bitmap: Bitmap = image
                .toBitmap()
                .rotateBitmap(image.imageInfo.rotationDegrees)
            // Crop the bitmap according the view size
            val crop = with(bitmap) {
                val screenScale = screenSize.width / screenSize.height
                val imageScale = width.toFloat() / height.toFloat()
                if (screenScale < imageScale)
                    // Fit by height (limited by height)
                    Bitmap.createBitmap(
                        this,0,0,
                        (if(screenScale<1) height*screenScale else height/screenScale).roundToInt(),
                        height
                    )
                else
                    // Fit by width
                    Bitmap.createBitmap(
                        this,0,0,
                        width,
                        (if(screenScale<1) width*screenScale else width/screenScale).roundToInt()
                    )
            }
            image.close()
            // Raise event
            onPhotoCaptured(crop)
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e(TAG, "Error capturing image", exception)
        }
    })
}