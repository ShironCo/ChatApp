package com.example.chatapps.navegation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapps.components.SplashScreen
import com.example.chatapps.components.createUser.presentation.UserScreen
import com.example.chatapps.components.createUser.presentation.createProfile.ProfileScreen
import com.example.chatapps.components.createUser.presentation.selectCountry.SelectCountryApp

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavigation(){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = AppScreens.Splash.route){
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
        composable(AppScreens.Splash.route){
            SplashScreen(navHostController)
        }
    }
}