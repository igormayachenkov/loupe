package ru.igormayachenkov.loupe.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset

data class ScreenScale(
    val sx : Float = 1f,
    val sy : Float = 1f
) {
    constructor(screenSize:Size, dataSize:Size)
            : this(screenSize.width/dataSize.width, screenSize.height/dataSize.height)

    fun dataToScreen(data:Offset):Offset =
        Offset(sx * data.x, sy * data.y)

    fun screenToData(screen:Offset):Offset =
        Offset(screen.x / sx, screen.y / sy)

}