package com.example.chatapps.components.createUser.presentation.mainChat.addContact

sealed interface AddContactEvents{

    data class ChangeCode(val code:String): AddContactEvents
    data class ChangeFlag(val flag:String): AddContactEvents
    data class ChangeName(val name:String): AddContactEvents
    data class ChangeLastName(val lastName:String): AddContactEvents
    data class ChangeTelephone(val telephone:String): AddContactEvents
    data class ChangeExpanded(val expanded: Boolean): AddContactEvents

}