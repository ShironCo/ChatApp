package com.example.chatapps.components.createUser.presentation

import android.content.Context
import android.net.wifi.hotspot2.pps.Credential
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthCredential

sealed interface CreateUserEvents{
    object DialogToggle : CreateUserEvents
    object CodeToggle : CreateUserEvents
    object ProgressToggle : CreateUserEvents
    object ValidatePhone : CreateUserEvents
    object VibratePhone : CreateUserEvents
    data class ChangePhone(val phone: String) : CreateUserEvents
    data class ChangeCode(val code: String) : CreateUserEvents
    data class NavSelectCountry(val navHostController: NavHostController): CreateUserEvents
    data class NavBack(val navHostController: NavHostController): CreateUserEvents
    data class NavBackCountry(val navHostController: NavHostController, val code: String, val Flag: String):
        CreateUserEvents
    data class VerifyPhone(val context: Context, val phoneNumber: String): CreateUserEvents
    data class VerifyCredential(val context: Context, val navHostController: NavHostController):
        CreateUserEvents
    data class GoProfile(val navHostController: NavHostController): CreateUserEvents
}