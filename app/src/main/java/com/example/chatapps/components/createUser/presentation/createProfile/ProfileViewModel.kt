package com.example.chatapps.components.createUser.presentation.createProfile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
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
import com.example.chatapps.R
import com.example.chatapps.components.createUser.domain.usecases.UriCopyToUri
import com.example.chatapps.core.domain.preferences.Variable
import com.example.chatapps.core.domain.repository.CoreRepository
import com.example.chatapps.navegation.AppScreens
import com.example.chatapps.ui_common.CirculeProgress
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CoreRepository,
    private val uriCopyToUri: UriCopyToUri,
) : ViewModel() {

    private val fireStore = Firebase.firestore
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val user = Firebase.auth.currentUser
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
            is ProfileEvents.UpInfo -> {
                viewModelScope.launch {
                    if (state.name.isNotEmpty()) {
                        state = state.copy(
                            progressToggle = true
                        )
                        val riversRef = storageRef.child("images/photoProfile${user?.phoneNumber}")
                        val directory = File(events.context.filesDir, Variable.Perfil.key)
                        val archive = File(directory, Variable.FotoPerfil.key)
                        if (archive.exists()) {
                            riversRef.putFile(archive.toUri()).addOnSuccessListener {
                                user?.phoneNumber.let { phoneNumber ->
                                    val uID = user?.uid
                                    if (phoneNumber != null) {
                                        val users = hashMapOf(
                                            "fotoPerfil" to riversRef.path,
                                            "nombre" to state.name,
                                            "numero" to phoneNumber
                                        )
                                        if (uID != null){
                                            fireStore.collection("user").document(uID)
                                                .set(users)
                                                .addOnSuccessListener {
                                                    Log.d("Creacion de usuario", "Usuario creado")
                                                    events.navHostController.popBackStack()
                                                    events.navHostController.navigate(AppScreens.MainChat.route)
                                                    fireStore.terminate()
                                                }.addOnFailureListener { Er ->
                                                    Log.d(
                                                        "Creacion de usuario fallido",
                                                        "Ha ocurrido un error $Er"
                                                    )
                                                }
                                        }
                                    }
                                }
                                Log.d("Advert", "Ubicacion de la imagen ${riversRef.path}")
                                Log.d("Up", "Imagen correctamente subida")
                            }.addOnFailureListener {
                                Log.d("Down", "Ha ocurrido un problema $it")
                            }
                        } else {
                            val bitmap = BitmapFactory.decodeResource(
                                events.context.resources,
                                R.drawable.profilephoto
                            )
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val data = baos.toByteArray()
                            riversRef.putBytes(data).addOnSuccessListener {
                                Log.d("Up", "Imagen correctamente subida")
                            }.addOnFailureListener {
                                Log.d("Down", "Ha ocurrido un problema $it")
                            }
                        }
                    } else {
                        Toast.makeText(
                            events.context,
                            "El nombre no puede quedar vacio",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
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

