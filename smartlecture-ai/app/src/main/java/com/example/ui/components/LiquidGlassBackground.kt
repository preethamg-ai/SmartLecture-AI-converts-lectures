package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

/**
 * Liquid glass atmospheric background with animated glowing floating orbs
 * in Electric Blue, Neon Violet, and Bright Cyan.
 */
@Composable
fun LiquidGlassBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlowTransition")

    // Slow organic floating animations for orbs
    val orbOffset1X by infiniteTransition.animateFloat(
        initialValue = -40f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Orb1X"
    )

    val orbOffset1Y by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 70f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Orb1Y"
    )

    val orbOffset2X by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = -50f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Orb2X"
    )

    val orbOffset2Y by infiniteTransition.animateFloat(
        initialValue = 80f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(16000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Orb2Y"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        BgDeepDark,
                        BgMidnight,
                        Color(0xFF070B18)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Orb 1: Neon Cyan glow at top-left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BrightCyan.copy(alpha = 0.28f),
                        CyanAccent.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.2f + orbOffset1X, height * 0.18f + orbOffset1Y),
                    radius = (width * 0.55f) * pulseScale
                ),
                radius = (width * 0.55f) * pulseScale,
                center = Offset(width * 0.2f + orbOffset1X, height * 0.18f + orbOffset1Y)
            )

            // Orb 2: Electric Purple / Violet glow at center-right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonViolet.copy(alpha = 0.26f),
                        DeepViolet.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.82f + orbOffset2X, height * 0.48f + orbOffset2Y),
                    radius = (width * 0.65f) * pulseScale
                ),
                radius = (width * 0.65f) * pulseScale,
                center = Offset(width * 0.82f + orbOffset2X, height * 0.48f + orbOffset2Y)
            )

            // Orb 3: Electric Blue glow near bottom-left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.22f),
                        Color(0xFF0052D4).copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.35f - orbOffset2X, height * 0.82f + orbOffset1Y),
                    radius = width * 0.5f
                ),
                radius = width * 0.5f,
                center = Offset(width * 0.35f - orbOffset2X, height * 0.82f + orbOffset1Y)
            )
        }

        content()
    }
}
