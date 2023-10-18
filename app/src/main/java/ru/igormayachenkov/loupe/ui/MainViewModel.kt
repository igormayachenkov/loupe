package ru.igormayachenkov.loupe.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "myapp.ViewModel"


class MainViewModel : ViewModel() {

    init { Log.d(TAG, "init") }


    private val _imageFlow:MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val imageFlow:StateFlow<Bitmap?> = _imageFlow.asStateFlow()

    fun setImage(image:Bitmap){
        _imageFlow.value = image
    }

}