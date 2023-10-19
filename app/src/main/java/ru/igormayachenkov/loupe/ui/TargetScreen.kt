package ru.igormayachenkov.loupe.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableTarget(
    target: Offset,
    onDrag:(Offset)->Unit
) {
    val pxSize = LocalDensity.current.run { TARGET_SEMISIZE_DP.dp.toPx() }
    val pxHole = LocalDensity.current.run { TARGET_HOLE_DP.dp.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            Modifier
                .offset {
                    IntOffset((target.x - pxSize).roundToInt(), (target.y - pxSize).roundToInt())
                }
                //.background(Color.Blue)
                .size((2 * TARGET_SEMISIZE_DP).dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    }
                }
        ){
            // DRAW TARGET
            drawCircle(
                color = TARGET_COLOR,
                radius = 100f,
                center = center,
                style = Stroke(width = TARGET_LINE_WIDTH)
            )
            drawCircle(
                color = TARGET_COLOR,
                radius = 50f,
                center = center,
                style = Stroke(width = TARGET_LINE_WIDTH)
            )
            drawLine(TARGET_COLOR, Offset(center.x-pxHole, center.y), Offset(0f, center.y), TARGET_LINE_WIDTH)
            drawLine(TARGET_COLOR, Offset(center.x+pxHole, center.y), Offset(size.width, center.y), TARGET_LINE_WIDTH)
            drawLine(TARGET_COLOR, Offset(center.x, center.y-pxHole), Offset(center.x,0f), TARGET_LINE_WIDTH)
            drawLine(TARGET_COLOR, Offset(center.x, center.y+pxHole), Offset(center.x,size.height), TARGET_LINE_WIDTH)
        }
    }
}
