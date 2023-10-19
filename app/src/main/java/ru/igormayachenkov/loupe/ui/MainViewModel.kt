package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

private const val TAG = "myapp.ViewModel"

class MainViewModel : ViewModel() {

    //----------------------------------------------------------------------------------------------
    // IMAGE BITMAP
    private val _imageFlow:MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val imageFlow:StateFlow<Bitmap?> = _imageFlow.asStateFlow()

    fun setImage(image:Bitmap){
        _imageFlow.value = image
        setTargetPosition(Offset( image.width.toFloat()/2, image.height.toFloat()/2 ))
    }
    fun clearImage(){
        _imageFlow.value = null
    }

    //----------------------------------------------------------------------------------------------
    // TARGET POSITION
    private val _targetFlow:MutableStateFlow<Offset> = MutableStateFlow(Offset(0f,0f))
    val targetFlow:StateFlow<Offset> = _targetFlow.asStateFlow()

    private fun setTargetPosition(targetPosition:Offset){
        _targetFlow.value = targetPosition
    }

    fun moveTargetPosition(move:Offset){
        val value = _targetFlow.value
        // New value
        var x = value.x+move.x
        var y = value.y+move.y
        // Check borders
        if(x<0) x=0f
        if(y<0) y=0f
        _imageFlow.value?.let {
            if(x>it.width)  x=it.width.toFloat()
            if(y>it.height) y=it.height.toFloat()
        }
        // Update value
        _targetFlow.value = Offset(x, y)
    }

    fun share(){
        val value = _targetFlow.value
        Log.w(TAG,"Target position: ${value.x.roundToInt()}x${value.y.roundToInt()}")

    }

}