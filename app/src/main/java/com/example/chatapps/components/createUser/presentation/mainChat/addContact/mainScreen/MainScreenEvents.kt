package com.example.chatapps.components.createUser.presentation.mainChat.addContact.mainScreen

sealed interface MainScreenEvents {
    data class ChangeText(val text: MainScreenStates): MainScreenEvents
}
