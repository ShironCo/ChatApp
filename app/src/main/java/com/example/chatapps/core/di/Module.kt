package com.example.chatapps.core.di

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.example.chatapps.components.createUser.data.repository.CreateUserRepositoryImpl
import com.example.chatapps.components.createUser.domain.repository.CreateUserRepository
import com.example.chatapps.core.data.repository.CoreRepositoryImpl
import com.example.chatapps.core.domain.repository.CoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.InstallIn
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object Module {
    @Provides
    @Singleton
    fun provideAuthFirebase(): FirebaseAuth {
        return Firebase.auth
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Provides
    @Singleton
    fun provideVibrator(context: Application): Vibrator {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Provides
    @Singleton
    fun provideRepository(
        vibrator: Vibrator
    ): CreateUserRepository {
        return CreateUserRepositoryImpl(vibrator)
    }

    @Provides
    @Singleton
    fun provideRepositoryCore(
        preview: Preview,
        cameraProvider: ProcessCameraProvider,
        imageCapture: ImageCapture
    ): CoreRepository{
        return CoreRepositoryImpl(preview, cameraProvider, imageCapture)
    }

    @Provides
    @Singleton
    fun provideCameraProvider(context: Application): ProcessCameraProvider {
        return ProcessCameraProvider.getInstance(context).get()
    }

    @Provides
    @Singleton
    fun providePreview(): Preview {
        return Preview.Builder().build()
    }


    @Provides
    @Singleton
    fun provideImageCapture(): ImageCapture {
        return ImageCapture.Builder().build()
    }
}