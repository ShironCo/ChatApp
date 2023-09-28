package com.example.chatapps.navegation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapps.createUser.presentation.UserScreen
import com.example.chatapps.createUser.presentation.createProfile.ProfileScreen
import com.example.chatapps.createUser.presentation.selectCountry.SelectCountryApp

@Composable
fun AppNavegation(){

    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = AppScreens.CreateUser.route+"/{code}/{flag}"){
        composable(AppScreens.CreateUser.route+"/{code}/{flag}"){
            val code = it.arguments?.getString("code")
            val flag = it.arguments?.getString("flag")
            UserScreen(navHostController, code, flag)
        }
        composable(AppScreens.SelectCountry.route){
            SelectCountryApp(navHostController)
        }
        composable(AppScreens.Profile.route){
            ProfileScreen()
        }
    }
}