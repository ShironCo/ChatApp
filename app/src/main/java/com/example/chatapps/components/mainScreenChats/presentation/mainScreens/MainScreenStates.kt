package com.example.chatapps.components.mainScreenChats.presentation.mainScreens


data class MainScreenStates(
    val search: String? = null,
    val toggleSearch: Boolean = false,
    val selectedTabIndex: Int = 0,
    val toggleContact: Boolean = false,
    val list: List<String> = listOf("Chat", "Contactos"),
)
