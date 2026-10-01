package com.example.segath.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val GloomyDarkColorScheme = darkColorScheme(
    primary = DarkGreenPrimary,
    onPrimary = Color(0xFF003311),
    primaryContainer = DarkGreenSecondary,
    onPrimaryContainer = Color(0xFFB9F6CA),
    secondary = NeonGreen,
    onSecondary = Color(0xFF003311),
    background = GloomyBlack,
    onBackground = TextLight,
    surface = DarkSurface,
    onSurface = TextLight,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted,
    error = AlertRed,
    onError = Color.White
)

@Composable
fun SegathTheme(
    darkTheme: Boolean = true, // Force gloomy dark theme by default
    dynamicColor: Boolean = false, // Use our custom gloomy black & dark green theme
    content: @Composable () -> Unit
) {
    val colorScheme = GloomyDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
