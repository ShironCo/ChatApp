package com.example.chatapps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    background = DarkBlue,
    primary = Lila,
    onPrimary = ColorWave,
    secondary = LightDarkBlue,
    onSecondary = Orange,
    secondaryVariant = White
)

private val LightColorPalette = lightColors(
    background = DarkBlue,
    primary = Lila,
    onPrimary = ColorWave,
    secondary = LightDarkBlue,
    onSecondary = Orange,
    secondaryVariant = White

)

@Composable
fun ChatAppsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}