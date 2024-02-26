package com.example.chatapps.core.domain.preferences

sealed class Variable(
    val key: String
){
    object Credential: Variable("credential")
    object Perfil: Variable("perfil")
    object FotoPerfil: Variable("imagen_perfil.jpg")
}