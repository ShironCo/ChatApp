package com.example.chatapps.components.createUser.domain.usecases

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UriCopyToUri @Inject constructor(){
    operator fun invoke(context: Context, sourceUri: Uri, destinationUri: Uri): Result<Uri>{
        return try{
            val contentResolver: ContentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(sourceUri)
            val outputStream: OutputStream? = contentResolver.openOutputStream(destinationUri)
            inputStream?.use { input ->
                outputStream?.use { output ->
                    input.copyTo(output)
                }
            }
            Result.success(destinationUri)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}