package com.example.chatapps.core.domain.preferences

sealed class Variable(
    val key: String
){
    object Credential: Variable("credential")
    object Code: Variable("code")
}