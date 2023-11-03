package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun PreviewScreen(image: Bitmap, target: Offset){
    val semisize = LocalDensity.current.run { PREVIEW_SEMISIZE_DP.dp.toPx() }
    val pxHole   = LocalDensity.current.run { TARGET_HOLE_DP.dp.toPx() }

    // Left or right position on the screen
    var isRight:Boolean by remember { mutableStateOf(target.x<0.4*image.width)}
    if(target.x<0.4*image.width) isRight=true
    if(target.x>0.6*image.width) isRight=false

    // IMAGE PARAMETERS
    var x0 = 0
    var y0 = 0
    var x  = (target.x-semisize).roundToInt()
    var y  = (target.y-semisize).roundToInt()
    var w  = (2*semisize).roundToInt()
    var h = w
    // Check borders
    if(x<0){ x0=-x; w-=x0; x=0; }
    if(y<0){ y0=-y; h-=y0; y=0; }
    if(x+w > image.width)  w = image.width-x
    if(y+h > image.height) h = image.height-y

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(isRight) Arrangement.End else Arrangement.Start
    ) {
        Canvas(
            modifier = Modifier
                .size((2 * PREVIEW_SEMISIZE_DP).dp)
                .border(2.dp, Color.Black)
                .background(Color.Black)
        ) {
            // IMAGE
            val imageBitmap: ImageBitmap =
                Bitmap.createBitmap( image, x, y, w, h )
                    .asImageBitmap()
            drawImage(
                image = imageBitmap,
                topLeft = Offset(x0.toFloat(),y0.toFloat())
            )
            // TARGET
            drawLine(TARGET_COLOR, Offset(center.x-pxHole, center.y), Offset(0f, center.y), TARGET_LINE_WIDTH)
            drawLine(TARGET_COLOR, Offset(center.x+pxHole, center.y), Offset(size.width, center.y), TARGET_LINE_WIDTH)
            drawLine(TARGET_COLOR, Offset(center.x, center.y-pxHole), Offset(center.x,0f), TARGET_LINE_WIDTH)
            drawLine(TARGET_COLOR, Offset(center.x, center.y+pxHole), Offset(center.x,size.height), TARGET_LINE_WIDTH)
        }
    }
}
