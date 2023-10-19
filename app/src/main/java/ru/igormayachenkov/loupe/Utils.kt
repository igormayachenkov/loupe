package ru.igormayachenkov.loupe

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.ui.geometry.Size

/**
 * The rotationDegrees parameter is the rotation in degrees clockwise from the original orientation.
 */
fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 *  Bitmap float size
 */
val Bitmap.size: Size
    get()= Size(width.toFloat(),height.toFloat())
