package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

/**
 * Futuristic multi-layered liquid glowing AI orb.
 * Features rotation, breathing pulse, and particle nodes.
 */
@Composable
fun AiPulsingOrb(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "OrbTransition")

    // Slow rotation of outer quantum rings
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbRotation"
    )

    // Breathing scale
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbPulse"
    )

    // Secondary reverse ring rotation
    val reverseRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbReverseRotation"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val baseRadius = this.size.minDimension / 2f

            // Outer diffused ambient aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BrightCyan.copy(alpha = 0.45f),
                        NeonViolet.copy(alpha = 0.28f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 1.15f * pulseScale
                ),
                radius = baseRadius * 1.15f * pulseScale,
                center = center
            )

            // Core liquid glass orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        CyanAccent.copy(alpha = 0.85f),
                        ElectricBlue.copy(alpha = 0.7f),
                        DeepViolet.copy(alpha = 0.4f)
                    ),
                    center = center,
                    radius = baseRadius * 0.55f * pulseScale
                ),
                radius = baseRadius * 0.55f * pulseScale,
                center = center
            )
        }

        // Rotating outer quantum ring 1
        Canvas(
            modifier = Modifier
                .size(size * 0.85f)
                .rotate(rotation)
        ) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = this.size.minDimension / 2.2f

            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        CyanAccent,
                        Color.Transparent,
                        NeonViolet,
                        BrightCyan
                    )
                ),
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Reverse rotating ring 2
        Canvas(
            modifier = Modifier
                .size(size * 0.7f)
                .rotate(reverseRotation)
        ) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = this.size.minDimension / 2.3f

            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color.White.copy(alpha = 0.8f),
                        ElectricBlueLight,
                        Color.Transparent,
                        NeonPink
                    )
                ),
                radius = radius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}
