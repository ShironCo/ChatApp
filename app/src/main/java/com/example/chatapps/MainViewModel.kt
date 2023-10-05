package com.example.chatapps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(): ViewModel () {

    var splashLoading by mutableStateOf(true)
    private set

    fun splash(){
        viewModelScope.launch {
            splashLoading = false
        }
    }

}