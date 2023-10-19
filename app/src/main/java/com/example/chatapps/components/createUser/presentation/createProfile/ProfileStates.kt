package com.example.chatapps.components.createUser.presentation.createProfile

data class ProfileStates(
    val name: String = "",
    val camera: Boolean = false,
    val facing : Boolean = false,
    val picture: ByteArray = ByteArray(0)
    )