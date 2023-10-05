package com.example.chatapps.core.domain.preferences

import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.SharedPreferences
import android.os.Build
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesManager @Inject constructor(@ApplicationContext context: Context){

    private val sharedPreferencesProfile: SharedPreferences =
        context.getSharedPreferences("data_profile", Context.MODE_PRIVATE)

    fun getDataProfile(key: String, defaultValue: String = ""): String {
        return sharedPreferencesProfile.getString(key, defaultValue) ?: defaultValue
    }

    fun setDataProfile(key: String, value: String) {
        with(sharedPreferencesProfile.edit()) {
            putString(key, value)
            apply() // Se aplica el cambio de forma asíncrona
        }
    }

}

