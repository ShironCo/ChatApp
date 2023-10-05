package com.example.chatapps.navegation

sealed class AppScreens(
    val route: String
){

    object CreateUser : AppScreens("create_user")
    object SelectCountry : AppScreens("select_country")
    object Profile : AppScreens("profile")
    object Splash : AppScreens("splash_screen")
}