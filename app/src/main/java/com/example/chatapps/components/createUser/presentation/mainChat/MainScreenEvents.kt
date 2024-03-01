package com.example.chatapps.components.createUser.presentation.mainChat

sealed interface MainScreenEvents {
    data class ChangeText(val text: MainScreenStates): MainScreenEvents
}
