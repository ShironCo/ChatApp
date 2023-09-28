package com.example.chatapps.core.domain.preferences

import android.content.Context
import android.content.SharedPreferences
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