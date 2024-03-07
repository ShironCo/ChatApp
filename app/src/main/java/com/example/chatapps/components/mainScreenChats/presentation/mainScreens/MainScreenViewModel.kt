package com.example.chatapps.components.mainScreenChats.presentation.mainScreens

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
            is MainScreenEvents.ChangeToggleContact -> {
                states = states.copy(
                    toggleContact = events.toggleContact
                )
            }
            is MainScreenEvents.ChangeSearch -> {
                states = states.copy(
                    search = events.search
                )
            }
            is MainScreenEvents.ChangeSelectedTabIndex -> {
                states = states.copy(
                    selectedTabIndex = events.selectedTabIndex
                )
            }
            is MainScreenEvents.ChangeToggleSearch -> {
                states = states.copy(
                    toggleSearch = events.toggleSearch
                )
            }
        }
    }

}