package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

/**
 * Reusable Liquid Glass card with frosted sheen, soft borders,
 * inner highlights, and interactive micro-interactions.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    backgroundColor: Color = Color(0x1CFFFFFF),
    borderColor: Color = GlassBorderLight,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    isActive: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "GlassCardPressScale"
    )

    val activeBorder = if (isActive) {
        Brush.linearGradient(
            listOf(
                CyanAccent.copy(alpha = 0.9f),
                NeonViolet.copy(alpha = 0.8f),
                Color.Transparent
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                Color.White.copy(alpha = 0.30f),
                Color.White.copy(alpha = 0.08f),
                borderColor
            )
        )
    }

    val activeBackground = if (isActive) {
        Brush.linearGradient(
            listOf(
                Color(0x2800F2FE),
                Color(0x1C7C3AED),
                Color(0x180E172E)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                backgroundColor,
                backgroundColor.copy(alpha = (backgroundColor.alpha * 0.65f).coerceIn(0f, 1f))
            )
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = Color(0x33000000),
                spotColor = if (isActive) CyanAccent.copy(alpha = 0.35f) else Color(0x2200E5FF)
            )
            .clip(shape)
            .background(activeBackground)
            .border(
                border = BorderStroke(borderWidth, activeBorder),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = androidx.compose.material3.ripple(color = CyanAccent),
                        onClick = onClick
                    )
                } else Modifier
            )
    ) {
        // Inner highlight sheen at the very top edge of the glass card
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.White.copy(alpha = 0.09f),
                        0.25f to Color.Transparent
                    )
                )
        )
        content()
    }
}
