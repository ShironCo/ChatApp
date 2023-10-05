package com.example.chatapps.core.di

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import com.example.chatapps.components.createUser.data.repository.CreateUserRepositoryImpl
import com.example.chatapps.components.createUser.domain.repository.CreateUserRepository
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
    fun provideAuthFirebase(): FirebaseAuth{
        return Firebase.auth
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @Provides
    @Singleton
    fun provideVibrator(context: Application): Vibrator{
        return  context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Provides
    @Singleton
    fun provideRepository(vibrator: Vibrator): CreateUserRepository {
        return CreateUserRepositoryImpl(vibrator)
    }

}