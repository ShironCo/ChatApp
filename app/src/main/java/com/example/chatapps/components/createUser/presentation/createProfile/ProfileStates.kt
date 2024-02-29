package com.example.chatapps.components.createUser.presentation.createProfile

import android.graphics.Bitmap
import android.net.Uri

data class ProfileStates(
    val name: String = "",
    val camera: Boolean = false,
    val facing: Boolean = false,
    val previewPicture: Uri? = null,
    val pictureToggle: Boolean = false,
    val image: Bitmap? = null,
    val progressToggle: Boolean = false
    )