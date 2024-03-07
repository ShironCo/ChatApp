package com.example.chatapps.components.mainScreenChats.presentation.mainScreens

sealed interface MainScreenEvents {
    data class ChangeSearch(val search: String?): MainScreenEvents
    data class ChangeToggleSearch(val toggleSearch: Boolean): MainScreenEvents
    data class ChangeSelectedTabIndex(val selectedTabIndex: Int): MainScreenEvents

    data class ChangeToggleContact(val toggleContact: Boolean): MainScreenEvents
}
