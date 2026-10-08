package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

data class OnboardingStep(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val tag: String
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        OnboardingStep(
            title = "Turn lectures into smart notes.",
            description = "Upload audio or lecture documents. AI automatically extracts key concepts, formulas, and structured summaries.",
            icon = Icons.Filled.Description,
            tag = "DOCUMENT_SYNTHESIS"
        ),
        OnboardingStep(
            title = "Test your knowledge with AI quizzes.",
            description = "Generate exam-ready multiple choice assessments directly from your lecture topics with smart explanations.",
            icon = Icons.Filled.Psychology,
            tag = "INTELLIGENT_ASSESSMENT"
        ),
        OnboardingStep(
            title = "Understand where you need to improve.",
            description = "Track topic retention, pinpoint knowledge gaps, and get personalized revision recommendations.",
            icon = Icons.Filled.BarChart,
            tag = "LEARNING_ANALYTICS"
        )
    )

    var currentStepIndex by remember { mutableIntStateOf(0) }
    val step = steps[currentStepIndex]

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp)
                .testTag("screen_onboarding"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = "AI",
                        tint = CyanAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SmartLecture AI",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (currentStepIndex < steps.size - 1) {
                    TextButton(
                        onClick = onFinishOnboarding,
                        modifier = Modifier.testTag("onboarding_skip_button")
                    ) {
                        Text(
                            text = "Skip",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            // Central Visual Card
            AnimatedContent(
                targetState = currentStepIndex,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(300)) + slideInHorizontally { width -> width / 3 })
                        .togetherWith(fadeOut(animationSpec = tween(200)))
                },
                label = "OnboardingContent"
            ) { targetIndex ->
                val current = steps[targetIndex]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Glass Illustration Container
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        shape = RoundedCornerShape(28.dp),
                        backgroundColor = Color(0x18FFFFFF),
                        borderColor = CyanAccent.copy(alpha = 0.4f),
                        isActive = true
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AiPulsingOrb(size = 180.dp)

                            // Glass Badge with Feature Icon
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color.White.copy(alpha = 0.22f),
                                                Color(0x1500E5FF)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = current.icon,
                                    contentDescription = current.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = current.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = current.description,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Bottom Navigation & Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.indices.forEach { index ->
                        val isSelected = index == currentStepIndex
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (isSelected) 24.dp else 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isSelected) CyanAccent else Color(0x35FFFFFF))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                GlassPrimaryButton(
                    text = if (currentStepIndex == steps.size - 1) "Get Started →" else "Continue",
                    onClick = {
                        if (currentStepIndex < steps.size - 1) {
                            currentStepIndex++
                        } else {
                            onFinishOnboarding()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "onboarding_primary_button",
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
    }
}
