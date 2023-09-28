package com.example.chatapps.createUser.domain

import androidx.core.text.isDigitsOnly
import javax.inject.Inject

class PhoneValidator @Inject constructor(){
    operator fun invoke(phone:String): Boolean{
        return phone.isDigitsOnly()
    }
}