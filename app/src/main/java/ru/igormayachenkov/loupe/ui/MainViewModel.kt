package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.igormayachenkov.loupe.data.TargetPosition

private const val TAG = "myapp.ViewModel"


class MainViewModel : ViewModel() {

    init { Log.d(TAG, "init") }


    // IMAGE
    private val _imageFlow:MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val imageFlow:StateFlow<Bitmap?> = _imageFlow.asStateFlow()

    fun setImage(image:Bitmap){
        _imageFlow.value = image
        setTargetPosition(TargetPosition( image.width.toFloat()/2, image.height.toFloat()/2 ))
    }
    fun clearImage(){
        _imageFlow.value = null
    }

    // TARGET
    private val _targetFlow:MutableStateFlow<TargetPosition> = MutableStateFlow(TargetPosition())
    val targetFlow:StateFlow<TargetPosition> = _targetFlow.asStateFlow()

    fun setTargetPosition(targetPosition:TargetPosition){
        _targetFlow.value = targetPosition
    }

}