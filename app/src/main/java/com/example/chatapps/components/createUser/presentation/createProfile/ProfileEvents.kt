package com.example.chatapps.components.createUser.presentation.createProfile

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

sealed interface ProfileEvents {
    object SetFacing: ProfileEvents
    object SetCamera : ProfileEvents
    data class SetName(val name: String): ProfileEvents
    data class ShowPreview(val preview: PreviewView, val lifeCycleOwner: LifecycleOwner): ProfileEvents
    data class CapturePicture(val context: Context): ProfileEvents
}