package com.example.chatapps.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapps.R
import com.example.chatapps.navegation.AppScreens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController) {

    LaunchedEffect(key1 = true) {
        if (Firebase.auth.currentUser != null) {
            navHostController.popBackStack()
            navHostController.navigate(AppScreens.Profile.route)
        } else {
            navHostController.popBackStack()
            navHostController.navigate(AppScreens.CreateUser.route + "/+/ ")
        }
    }
}
