package com.example.chatapps.components.mainScreenChats.presentation.addContacts

data class AddContactStates(
    val code: String? = null,
    val name: String = "",
    val lastName: String = "",
    val telephone: String = "",
    val flag: String? = null,
    val expanded: Boolean = false,
    val errorTxphone: Boolean = false,
)
