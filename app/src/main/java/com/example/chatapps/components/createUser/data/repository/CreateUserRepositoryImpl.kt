package com.example.chatapps.components.createUser.data.repository


import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.chatapps.components.createUser.domain.repository.CreateUserRepository
import javax.inject.Inject

class CreateUserRepositoryImpl @Inject constructor(
    private val vibrator: Vibrator
) : CreateUserRepository {

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun vibrateShot() {
        val vibrationEffect : VibrationEffect =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect.createOneShot(1000, VibrationEffect.EFFECT_HEAVY_CLICK)
            } else {
                Log.e("TAG", "Cannot vibrate device..")
                TODO("VERSION.SDK_INT < O")
            }
        vibrator.cancel()
        vibrator.vibrate(vibrationEffect)
    }

}