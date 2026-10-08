package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuizResult
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun QuizResultScreen(
    result: QuizResult,
    onReviewAnswers: () -> Unit,
    onTryAgain: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var animatedScoreProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        animatedScoreProgress = (result.scorePercent / 100f).coerceIn(0f, 1f)
    }

    val animatedSweep by animateFloatAsState(
        targetValue = animatedScoreProgress * 360f,
        animationSpec = tween(durationMillis = 1200),
        label = "ScoreArcSweep"
    )

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_quiz_result")
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Top Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quiz Performance",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = onGoHome,
                    modifier = Modifier
                        .testTag("result_close_to_home")
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x18FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            // Large Circular Score Glass Hero
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                backgroundColor = Color(0x20152445),
                borderColor = CyanAccent.copy(alpha = 0.45f),
                isActive = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Circular Gauge
                    Box(
                        modifier = Modifier.size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(140.dp)) {
                            val strokeWidth = 12.dp.toPx()
                            val arcRadius = (size.minDimension - strokeWidth) / 2f
                            val topLeft = Offset(
                                (size.width - arcRadius * 2) / 2,
                                (size.height - arcRadius * 2) / 2
                            )

                            // Track
                            drawArc(
                                color = Color(0x25FFFFFF),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                                topLeft = topLeft,
                                size = Size(arcRadius * 2, arcRadius * 2)
                            )

                            // Animated Active Gradient Arc
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(BrightCyan, ElectricBlueLight, NeonViolet, BrightCyan)
                                ),
                                startAngle = -90f,
                                sweepAngle = animatedSweep,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                                topLeft = topLeft,
                                size = Size(arcRadius * 2, arcRadius * 2)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${result.scorePercent}%",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Score",
                                fontSize = 11.sp,
                                color = CyanAccent,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (result.scorePercent >= 80) "Excellent work!" else if (result.scorePercent >= 60) "Good Effort!" else "Revision Recommended",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "AI analyzed your response patterns and confidence signals",
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 4 Key Statistics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ResultMetricItem(
                            label = "Correct",
                            value = "${result.correctCount}",
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricItem(
                            label = "Incorrect",
                            value = "${result.incorrectCount}",
                            color = NeonPink,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricItem(
                            label = "Accuracy",
                            value = "${result.accuracy}%",
                            color = BrightCyan,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricItem(
                            label = "Time",
                            value = result.timeTaken,
                            color = ElectricBlueLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Topics You Understand vs Topics To Revise
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Understand
                GlassCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color(0x1810B981)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✅", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Understood",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        result.understoodTopics.forEach { topic ->
                            Text(
                                text = "• $topic",
                                fontSize = 11.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Revise
                GlassCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color(0x18EC4899)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚠️", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "To Revise",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonPink
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        result.topicsToRevise.forEach { topic ->
                            Text(
                                text = "• $topic",
                                fontSize = 11.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // AI Recommendation Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                backgroundColor = Color(0x22131F3B),
                borderColor = NeonViolet.copy(alpha = 0.4f),
                isActive = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "AI",
                                tint = NeonViolet,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI RECOMMENDATION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonViolet,
                                letterSpacing = 1.sp
                            )
                        }

                        AiPulsingOrb(size = 36.dp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = result.aiRecommendation,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }

            // Buttons: Review Answers & Try Again
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassPrimaryButton(
                    text = "Review Answers",
                    onClick = onReviewAnswers,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "result_review_answers_button"
                )

                GlassSecondaryButton(
                    text = "Try Again",
                    onClick = onTryAgain,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "result_try_again_button"
                )
            }
        }
    }
}

@Composable
private fun ResultMetricItem(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x14FFFFFF))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}
