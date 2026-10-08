package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SplashGlow")

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowScale"
    )

    LaunchedEffect(Unit) {
        delay(2200) // Brief animated introduction
        onSplashFinished()
    }

    LiquidGlassBackground(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("screen_splash"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Animated Glowing AI Logo Container
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(glowScale),
                    contentAlignment = Alignment.Center
                ) {
                    AiPulsingOrb(size = 140.dp)

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(16.dp, CircleShape, spotColor = CyanAccent)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.25f),
                                        Color(0x2000E5FF)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "SmartLecture Logo",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Brand Name
                Text(
                    text = "SmartLecture AI",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline
                Text(
                    text = "Learn smarter. Remember more.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = CyanAccent.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
