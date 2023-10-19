package com.example.chatapps.components.createUser.presentation

data class CreateUserStates (
    val phone : String = "",
    val phoneError: Boolean = false,
    val dialogToggle: Boolean = false,
    val sendCodeToggle: Boolean = false,
    val ProgressToggle: Boolean = false,
    val code: String = "",
    val codeError: Boolean = false,
)