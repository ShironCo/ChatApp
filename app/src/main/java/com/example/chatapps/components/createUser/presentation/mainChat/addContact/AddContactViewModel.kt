package com.example.chatapps.components.createUser.presentation.mainChat.addContact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(

) : ViewModel() {

    var state by mutableStateOf(AddContactStates())
        private set

    fun onEvent(events: AddContactEvents) {
        when (events) {
            is AddContactEvents.ChangeCode -> {
                state = state.copy(
                    code = events.code
                )
            }
            is AddContactEvents.ChangeExpanded -> {
                state = state.copy(
                    expanded = events.expanded
                )
            }
            is AddContactEvents.ChangeFlag -> {
                state = state.copy(
                    flag = events.flag
                )
            }
            is AddContactEvents.ChangeLastName -> {
                state = state.copy(
                    lastName = events.lastName
                )
            }
            is AddContactEvents.ChangeName -> {
                state = state.copy(
                    name = events.name
                )
            }
            is AddContactEvents.ChangeTelephone -> {
                state = state.copy(
                    telephone = events.telephone
                )
            }
        }
    }

}