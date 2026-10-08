package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = TextDark,
    primaryContainer = DeepViolet,
    onPrimaryContainer = TextPrimary,
    secondary = NeonViolet,
    onSecondary = TextPrimary,
    secondaryContainer = Color(0xFF1E1B4B),
    onSecondaryContainer = TextSecondary,
    tertiary = BrightCyan,
    onTertiary = TextDark,
    background = BgDeepDark,
    onBackground = TextPrimary,
    surface = BgMidnight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF161F36),
    onSurfaceVariant = TextSecondary,
    outline = GlassBorderLight,
    outlineVariant = GlassBorderSubtle
)

@Composable
fun SmartLectureTheme(
    darkTheme: Boolean = true, // Default to futuristic dark liquid glass theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
