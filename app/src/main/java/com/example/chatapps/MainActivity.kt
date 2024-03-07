package com.example.chatapps

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.chatapps.components.createUser.presentation.createProfile.ProfileScreen
import com.example.chatapps.components.mainScreenChats.presentation.mainScreens.MainScreenChat
import com.example.chatapps.navegation.AppNavigation
import com.example.chatapps.ui.theme.ChatAppsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.splashLoading
        }
        viewModel.splash()
        setContent {
            ChatAppsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   // ProfileScreen()
                    AppNavigation()
                    //MainScreenChat()
                }
            }
        }
    }
}


