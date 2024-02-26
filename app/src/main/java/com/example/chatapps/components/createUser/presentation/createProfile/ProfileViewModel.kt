package com.example.chatapps.components.createUser.presentation.createProfile

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapps.components.createUser.domain.usecases.UriCopyToUri
import com.example.chatapps.core.domain.preferences.Variable
import com.example.chatapps.core.domain.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val uriCopyToUri: UriCopyToUri,
) : ViewModel() {

    var state by mutableStateOf(ProfileStates())
        private set

    fun onEvent(events: ProfileEvents) {
        when (events) {
            is ProfileEvents.SetName -> {
                state = state.copy(
                    name = events.name
                )
            }
            ProfileEvents.SetCamera -> {
                state = state.copy(
                    camera = !state.camera,
                    facing = false
                )
            }
            is ProfileEvents.ShowPreview -> {
                val preview = events.preview
                val lifeCycle = events.lifeCycleOwner
                viewModelScope.launch {
                    repository.showCameraPreview(preview, lifeCycle, state.facing)
                }
            }
            is ProfileEvents.CapturePicture -> {
                viewModelScope.launch {
                    createFile(events.context)
                    val outPutImage = state.previewPicture?.let {
                        ImageCapture.OutputFileOptions.Builder(
                            it.toFile()
                        ).build()
                    }
                    if (outPutImage != null) {
                        repository.captureImage().takePicture(
                            outPutImage,
                            ContextCompat.getMainExecutor(events.context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    state = state.copy(
                                        previewPicture = outputFileResults.savedUri,
                                        pictureToggle = true
                                    )
                                }
                                override fun onError(exception: ImageCaptureException) {
                                    Toast.makeText(
                                        events.context,
                                        "Almacenamiento lleno",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        )
                    }
                }
            }
            ProfileEvents.SetFacing -> {
                viewModelScope.launch {
                    state = state.copy(
                        facing = !state.facing
                    )
                }
            }
            ProfileEvents.SetPictureToggle -> {
                state = state.copy(
                    pictureToggle = !state.pictureToggle
                )
            }
            is ProfileEvents.SetImagePreview -> {
                viewModelScope.launch {
                    createFile(events.context)
                    state.previewPicture?.let { previewUri ->
                        uriCopyToUri(events.context, events.uri, previewUri).onSuccess {
                            val bitmap = BitmapFactory.decodeStream(
                                events.context.contentResolver.openInputStream(it)
                            )
                            state = state.copy(
                                image = bitmap
                            )
                        }
                    }
                }
            }
            is ProfileEvents.Init -> {

            }
            is ProfileEvents.DonePicture -> {
                val bitmap = BitmapFactory.decodeStream(
                    events.context.contentResolver.openInputStream(state.previewPicture!!)
                )
                viewModelScope.launch {
                    state = state.copy(
                        image = bitmap,
                        pictureToggle = false,
                        camera = false
                    )
                }
            }
            is ProfileEvents.UpdateInfo -> TODO()
        }
    }

    private fun createFile(context: Context) {
        val directory = File(context.filesDir, Variable.Perfil.key)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val cacheDir = context.cacheDir
        val imageFile = File(cacheDir, Variable.FotoPerfil.key)
        imageFile.delete()
        val archive = File(directory, Variable.FotoPerfil.key)
        archive.let {
            state = state.copy(
                previewPicture = it.toUri()
            )
        }
    }
}

