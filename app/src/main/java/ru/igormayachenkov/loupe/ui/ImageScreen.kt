package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import ru.igormayachenkov.loupe.data.TargetPosition
import kotlin.math.roundToInt

@Composable
fun ImageScreen(
    image:Bitmap,
    viewModel: MainViewModel
) {
//    val imageBitmap: ImageBitmap = remember(image.hashCode()) { image.asImageBitmap() }
    val target: TargetPosition by viewModel.targetFlow.collectAsState()

//    Image(
//        modifier = Modifier.fillMaxSize(),
//        bitmap = imageBitmap,
//        contentDescription = "Last captured photo",
//        contentScale = ContentScale.FillBounds
//    )


    Canvas(
        modifier = Modifier.fillMaxSize(),
    ){
        val sx = size.width /image.width
        val sy = size.height/image.height

        // IMAGE
        val imageBitmap: ImageBitmap = image
            .scale((sx * image.width).roundToInt(), (sy * image.height).roundToInt())
            .asImageBitmap()
        drawImage(imageBitmap)

        // TARGET
        drawTarget(this,sx*target.x, sy*target.y)
    }

    // TOOLBAR
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "image ${image.width}x${image.height}")
        Button(onClick = viewModel::clearImage) {
            Text("Clear")
        }
    }
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