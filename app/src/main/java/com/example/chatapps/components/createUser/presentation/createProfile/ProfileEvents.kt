package com.example.chatapps.components.createUser.presentation.createProfile

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.example.chatapps.components.createUser.presentation.CreateUserEvents

sealed interface ProfileEvents {
    object SetFacing : ProfileEvents
    object SetCamera : ProfileEvents
    object SetPictureToggle : ProfileEvents
    data class SetName(val name: String) : ProfileEvents
    data class ShowPreview(val preview: PreviewView, val lifeCycleOwner: LifecycleOwner) :
        ProfileEvents
    data class CapturePicture(val context: Context, val imageLoader: ImageLoader) : ProfileEvents
    data class SetImagePreview(val uri: Uri, val context: Context, val imageLoader: ImageLoader) : ProfileEvents
    data class UpInfo(val context: Context, val navHostController: NavHostController): ProfileEvents
    data class Init(val context: Context): ProfileEvents
    data class DonePicture(val context: Context): ProfileEvents
}