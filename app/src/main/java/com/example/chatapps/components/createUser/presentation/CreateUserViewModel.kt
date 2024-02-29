package com.example.chatapps.components.createUser.presentation

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.chatapps.core.domain.preferences.PreferencesManager
import com.example.chatapps.core.domain.preferences.Variable
import com.example.chatapps.components.createUser.domain.repository.CreateUserRepository
import com.example.chatapps.components.createUser.domain.usecases.PhoneValidator
import com.example.chatapps.navegation.AppScreens
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val auth: FirebaseAuth,
    private val phoneValidator: PhoneValidator,
    private val repository: CreateUserRepository
) : ViewModel() {

    var state by mutableStateOf(CreateUserStates())
        private set

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // Implementa los métodos de devolución de llamada según tus necesidades
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Acción después de la verificación exitosa
            Log.d(TAG, "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            // Acción en caso de error en la verificación
            Log.w(TAG, "onVerificationFailed", exception)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            preferencesManager.setDataProfile(Variable.Credential.key, verificationId)
            onEvent(CreateUserEvents.ProgressToggle)
            state = state.copy(
                sendCodeToggle = true
            )
            Log.d(TAG, "onCodeSent:$verificationId")
        }
    }

    fun onEvent(events: CreateUserEvents) {
        when (events) {
            CreateUserEvents.ValidatePhone -> {
                val phoneValidator = phoneValidator(state.phone)
                state = state.copy(
                    phoneError = !phoneValidator
                )
                if (phoneValidator) {
                    state = state.copy(
                        dialogToggle = !state.dialogToggle
                    )
                }
            }
            is CreateUserEvents.ChangePhone -> {
                state = state.copy(
                    phone = events.phone
                )
            }
            is CreateUserEvents.NavSelectCountry -> {
                events.navHostController.popBackStack()
                events.navHostController.navigate(AppScreens.SelectCountry.route)
            }
            is CreateUserEvents.NavBack -> {
                events.navHostController.navigate(AppScreens.CreateUser.route + "/+/ ") {
                    popUpTo(AppScreens.SelectCountry.route) {
                        inclusive = true
                    }
                }
            }
            is CreateUserEvents.NavBackCountry -> {
                val code = events.code
                val flag = events.Flag
                events.navHostController.navigate(AppScreens.CreateUser.route + "/$code/$flag") {
                    popUpTo(AppScreens.SelectCountry.route) {
                        inclusive = true
                    }
                }
            }
            CreateUserEvents.DialogToggle -> {
                state = state.copy(
                    dialogToggle = !state.dialogToggle
                )
            }
            is CreateUserEvents.VerifyPhone -> {
                viewModelScope.launch {
                    sendCode(events.context, events.phoneNumber)
                }
            }
            CreateUserEvents.CodeToggle -> {
                state = state.copy(
                    sendCodeToggle = !state.sendCodeToggle
                )
            }
            is CreateUserEvents.ChangeCode -> {
                state = state.copy(
                    code = state.code + events.code
                )
            }
            is CreateUserEvents.VerifyCredential -> {
                viewModelScope.launch {
                    state = state.copy(
                        codeError = false,
                        ProgressToggle = true
                    )
                    val credential = preferencesManager.getDataProfile(Variable.Credential.key)
                    credential.let {
                        val phoneAuth = PhoneAuthProvider.getCredential(credential, state.code)
                        signInWithPhoneAuthCredential(
                            phoneAuth,
                            events.context,
                            events.navHostController
                        )
                    }
                }
            }
            CreateUserEvents.ProgressToggle -> {
                state = state.copy(
                    ProgressToggle = !state.ProgressToggle
                )
            }
            CreateUserEvents.VibratePhone -> {
                viewModelScope.launch {
                    repository.vibrateShot()
                }
            }
            is CreateUserEvents.GoProfile -> {
                events.navHostController.popBackStack()
                events.navHostController.navigate(AppScreens.Profile.route)
            }
        }
    }

    private suspend fun sendCode(
        context: Context,
        phoneNumber: String
    ) {
        withContext(Dispatchers.IO) {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(context as Activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        context: Context,
        navHostController: NavHostController
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    onEvent(CreateUserEvents.GoProfile(navHostController))
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        state = state.copy(
                            code = "",
                            ProgressToggle = false,
                            codeError = true
                        )
                        onEvent(CreateUserEvents.VibratePhone)
                    }
                }
            }
    }
}