package com.example.chatapps.components.mainScreenChats.presentation.addContacts

import android.content.ContentValues.TAG
import android.util.Log
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
            AddContactEvents.CleanVariables -> {
                state = state.copy(
                    code = null,
                    flag = null,
                    expanded = false,
                    lastName = "",
                    name = "",
                    telephone = "",
                    errorTxphone = false
                )
            }
            AddContactEvents.AddContact -> {
                if ((!validatePhone(state.telephone) || state.telephone.isEmpty())){
                    state = state.copy(
                        errorTxphone = true
                    )
                }else{
                    Log.d(TAG, "USUARIO CREADO")
                }
            }
        }
    }

    private fun validatePhone(phone: String):Boolean{
        val regex = Regex("""^\d{10}$""")
        return phone.matches(regex)
    }

}