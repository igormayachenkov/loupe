package ru.igormayachenkov.loupe.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import ru.igormayachenkov.loupe.size
import kotlin.math.roundToInt

private const val TAG = "myapp.ImageScreen"

@Composable
fun ImageScreen(
    image:Bitmap,
    viewModel: MainViewModel
) {
//    val imageBitmap: ImageBitmap = remember(image.hashCode()) { image.asImageBitmap() }
    val target: Offset by viewModel.targetFlow.collectAsState()
    var canvas: Size by remember { mutableStateOf(Size(0f,0f)) }
    var scale:  ScreenScale by remember { mutableStateOf(ScreenScale()) }

//    Image(
//        modifier = Modifier.fillMaxSize(),
//        bitmap = imageBitmap,
//        contentDescription = "Last captured photo",
//        contentScale = ContentScale.FillBounds
//    )


    Canvas(
        modifier = Modifier.fillMaxSize(),
    ){
        //Log.d(TAG,"size: ${size.width}x${size.height}")
        scale = ScreenScale(this.size, image.size)
        Log.d(TAG,"scale $scale ")
        canvas = Size(size.width, size.height)

        // IMAGE
        val imageBitmap: ImageBitmap = image
            .scale(size.width.roundToInt(), size.height.roundToInt())
            .asImageBitmap()
        drawImage(imageBitmap)

        // TARGET
        //drawTarget(this,sx*target.x, sy*target.y)
    }

    // TOOLBAR
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // INFO
        Text(text = "image  ${image.width}x${image.height}",   color = Color.Yellow)
        Text(text = "canvas ${canvas.width.roundToInt()}x${canvas.height.roundToInt()}", color = Color.Yellow)
        Text(text = "target ${target.x.roundToInt()}x${target.y.roundToInt()}", color = Color.Yellow)
        // TOOLBAR
        val context = LocalContext.current
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = viewModel::clearImage) { Text("Clear") }
            Button(onClick = {copyToClipboard(target,context)} ) { Text("Share") }
        }
        //ScreenSize()
    }

    // TARGET
    DraggableTarget(
        scale.dataToScreen(target),
        onDrag = {dragAmount->
            val dataAmount = scale.screenToData(dragAmount)
            //Log.d(TAG,"drag3 $dragAmount $scale $dataAmount")
            viewModel.moveTargetPosition(dataAmount)
        }
    )

    // PREVIEW
    PreviewScreen(image,target)
}

private fun copyToClipboard(target:Offset, context: Context){
    // Copy to clipboard
    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.let{
        val clip = ClipData.newPlainText("Target position","${target.x.roundToInt()}x${target.y.roundToInt()}")
        it.setPrimaryClip(clip)
    }
    // Show Info
    Toast.makeText(context, "The target position is copied to the clipboard", Toast.LENGTH_SHORT).show()
}


@Composable
fun ScreenSize() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp

    val widthInPx = with(density) { widthInDp.roundToPx() }
    val heightInPx = with(density) { heightInDp.roundToPx() }

    Text(text = "widthInPx = $widthInPx, heightInPx = $heightInPx")
}

