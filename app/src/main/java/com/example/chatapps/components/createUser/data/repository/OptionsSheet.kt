package com.example.chatapps.components.createUser.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.chatapps.R

data class OptionsSheet(
    val title: Int,
    val icon: ImageVector
) {
    companion object {
        val listBottom = mutableListOf<OptionsSheet>().apply {
            add(
                OptionsSheet(
                    title = R.string.bottomSheetGallery,
                    icon = Icons.Default.Collections
                )
            )
            add(
                OptionsSheet(
                    title = R.string.bottomSheetCamera,
                    icon = Icons.Default.PhotoCamera
                )
            )
        }
    }
}