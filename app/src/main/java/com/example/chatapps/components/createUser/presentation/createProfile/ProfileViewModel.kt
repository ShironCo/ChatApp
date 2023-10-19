package com.example.chatapps.components.createUser.presentation.createProfile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapps.core.domain.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val repository: CoreRepository
): ViewModel(){

    var state by mutableStateOf(ProfileStates())
    private set

    fun onEvent(events: ProfileEvents){
        when(events){
            is ProfileEvents.SetName -> {
                state = state.copy(
                    name = events.name
                )
            }
            ProfileEvents.SetCamera -> {
                state = state.copy(
                    camera = !state.camera
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
//                viewModelScope.launch {
//                    repository.captureImage().takePicture(
//
//                    )
//                }
             }
            ProfileEvents.SetFacing -> {
                viewModelScope.launch {
                    state = state.copy(
                        facing = !state.facing
                    )
                }
            }
        }
    }







}