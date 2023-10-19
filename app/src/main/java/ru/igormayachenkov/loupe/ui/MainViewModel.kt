package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "myapp.ViewModel"


class MainViewModel : ViewModel() {

    init { Log.d(TAG, "init") }


    // IMAGE
    private val _imageFlow:MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val imageFlow:StateFlow<Bitmap?> = _imageFlow.asStateFlow()

    fun setImage(image:Bitmap){
        _imageFlow.value = image
        setTargetPosition(Offset( image.width.toFloat()/2, image.height.toFloat()/2 ))
    }
    fun clearImage(){
        _imageFlow.value = null
    }

    // TARGET
    private val _targetFlow:MutableStateFlow<Offset> = MutableStateFlow(Offset(0f,0f))
    val targetFlow:StateFlow<Offset> = _targetFlow.asStateFlow()

    fun setTargetPosition(targetPosition:Offset){
        _targetFlow.value = targetPosition
    }
    fun moveTargetPosition(move:Offset){
        val value = _targetFlow.value
        _targetFlow.value = Offset(value.x+move.x, value.y+move.y)
    }

}