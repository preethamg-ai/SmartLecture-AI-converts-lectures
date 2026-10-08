package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Deep Midnight and Obsidian Backgrounds
val BgDeepDark = Color(0xFF060913)
val BgMidnight = Color(0xFF0A1022)
val BgSurfaceGlass = Color(0x18FFFFFF)
val BgSurfaceGlassElevated = Color(0x28FFFFFF)
val BgSurfaceGlassSubtle = Color(0x0EFFFFFF)

// Border highlights for liquid glass
val GlassBorderLight = Color(0x35FFFFFF)
val GlassBorderSubtle = Color(0x1FFFFFFF)
val GlassBorderAccent = Color(0x5500F2FE)

// Future Accents
val CyanAccent = Color(0xFF00F2FE)
val BrightCyan = Color(0xFF00E5FF)
val ElectricBlue = Color(0xFF2979FF)
val ElectricBlueLight = Color(0xFF4FACFE)
val NeonViolet = Color(0xFF8B5CF6)
val PurpleAccent = Color(0xFF7C3AED)
val DeepViolet = Color(0xFF6D28D9)
val NeonPink = Color(0xFFEC4899)
val SuccessGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFCBD5E1)
val TextMuted = Color(0xFF94A3B8)
val TextDark = Color(0xFF0F172A)

// Gradients
val GradientBlueViolet = Brush.linearGradient(
    listOf(ElectricBlueLight, NeonViolet)
)

val GradientVioletCyan = Brush.linearGradient(
    listOf(NeonViolet, CyanAccent)
)

val GradientCyanBlue = Brush.linearGradient(
    listOf(CyanAccent, ElectricBlue)
)

val GradientAiOrb = Brush.radialGradient(
    listOf(
        CyanAccent.copy(alpha = 0.9f),
        ElectricBlue.copy(alpha = 0.7f),
        NeonViolet.copy(alpha = 0.4f),
        Color.Transparent
    )
)

val GradientGlassCard = Brush.linearGradient(
    listOf(
        Color(0x24FFFFFF),
        Color(0x12FFFFFF),
        Color(0x0A00E5FF)
    )
)

val GradientActiveCard = Brush.linearGradient(
    listOf(
        Color(0x2E00F2FE),
        Color(0x1E7C3AED),
        Color(0x180A1022)
    )
)
