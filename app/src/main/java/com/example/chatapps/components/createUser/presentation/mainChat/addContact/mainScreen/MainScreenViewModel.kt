package com.example.chatapps.components.createUser.presentation.mainChat.addContact.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(

) : ViewModel() {

    var states by mutableStateOf(MainScreenStates())
        private set

    fun onEvent(events: MainScreenEvents
    ){
        when(events){
            is MainScreenEvents.ChangeText -> {
               states = events.text
            }
        }
    }

}