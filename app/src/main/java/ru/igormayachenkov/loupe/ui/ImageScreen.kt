package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
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
        Text(text = "image  ${image.width}x${image.height}",   color = Color.Yellow)
        Text(text = "canvas ${canvas.width.roundToInt()}x${canvas.height.roundToInt()}", color = Color.Yellow)
        Text(text = "target ${target.x.roundToInt()}x${target.y.roundToInt()}", color = Color.Yellow)
        Button(onClick = viewModel::clearImage) {
            Text("Clear")
        }
        ScreenSize()
    }

    // TARGET
    DraggableTarget(
        scale.dataToScreen(target),
        onDrag = {dragAmount->
            val dataAmount = scale.screenToData(dragAmount)
            Log.d(TAG,"drag3 $dragAmount $scale $dataAmount")
            viewModel.moveTargetPosition(dataAmount)
        }
    )

}

val Bitmap.size:Size
    get()=Size(width.toFloat(),height.toFloat())

const val TARGET_ICON_SIZE_DP = 50 // semi size

@Composable
private fun DraggableTarget(
    target: Offset,
    onDrag:(Offset)->Unit
) {
    val pxSize = LocalDensity.current.run { TARGET_ICON_SIZE_DP.dp.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            Modifier
                .offset {
                    IntOffset((target.x-pxSize).roundToInt(), (target.y-pxSize).roundToInt())
                }
                //.background(Color.Blue)
                .size((2*TARGET_ICON_SIZE_DP).dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    }
                }
        ){
            // DRAW TARGET
            val color = Color.Red
            val width = 10f
            drawCircle(
                color = color,
                radius = 100f,
                center = center,
                style = Stroke(width = width)
            )
            drawCircle(
                color = color,
                radius = 50f,
                center = center,
                style = Stroke(width = width)
            )
            drawLine(color, Offset(center.x-10, center.y), Offset(0f, center.y), width)
            drawLine(color, Offset(center.x+10, center.y), Offset(size.width, center.y), width)
            drawLine(color, Offset(center.x, center.y-10), Offset(center.x,0f), width)
            drawLine(color, Offset(center.x, center.y+10), Offset(center.x,size.height), width)
        }
    }
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


private fun drawTarget(drawScope: DrawScope, x:Float, y:Float){
    val color = Color.Red
    val width = 10f
    val center = Offset(x,y)
    drawScope.apply {
        drawCircle(
            color = color,
            radius = 100f,
            center = center,
            style = Stroke(width = width)
        )
        drawCircle(
            color = color,
            radius = 50f,
            center = center,
            style = Stroke(width = width)
        )
        drawLine(color, Offset(x+10, y), Offset(x+150, y), width)
        drawLine(color, Offset(x-10, y), Offset(x-150, y), width)
        drawLine(color, Offset(x, y+10), Offset(x, y+150), width)
        drawLine(color, Offset(x, y-10), Offset(x, y-150), width)
    }
}