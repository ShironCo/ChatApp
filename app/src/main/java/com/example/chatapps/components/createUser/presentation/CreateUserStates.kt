package com.example.chatapps.components.createUser.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState

data class CreateUserStates @OptIn(ExperimentalMaterialApi::class) constructor(
    val phone : String = "",
    val phoneError: Boolean = false,
    val dialogToggle: Boolean = false,
    val sendCodeToggle: Boolean = false,
    val ProgressToggle: Boolean = false,
    val code: String = "",
    val codeError: Boolean = false,
)