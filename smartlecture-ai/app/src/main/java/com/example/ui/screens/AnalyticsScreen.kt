package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnalyticsData
import com.example.service.SmartLectureService
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier
) {
    val service = SmartLectureService.instance
    val analytics by service.analytics.collectAsState()

    var startChartAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startChartAnimation = true
    }

    val chartAnimFraction by animateFloatAsState(
        targetValue = if (startChartAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "ChartAnimFraction"
    )

    LiquidGlassBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_analytics"),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Screen Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Learning Analytics",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "AI performance evaluation & study trends",
                            fontSize = 12.sp,
                            color = CyanAccent
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x18FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.TrendingUp,
                            contentDescription = "Analytics",
                            tint = CyanAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // 4 Top Analytics Cards (2x2)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AnalyticsMetricCard(
                            label = "Study Time",
                            value = analytics.totalStudyTime,
                            icon = Icons.Filled.Schedule,
                            accentColor = BrightCyan,
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsMetricCard(
                            label = "Quiz Accuracy",
                            value = "${analytics.quizAccuracy}%",
                            icon = Icons.Filled.BarChart,
                            accentColor = NeonViolet,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AnalyticsMetricCard(
                            label = "Lectures Done",
                            value = "${analytics.lecturesCompleted}",
                            icon = Icons.Filled.School,
                            accentColor = ElectricBlueLight,
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsMetricCard(
                            label = "Learning Streak",
                            value = "${analytics.learningStreak} Days",
                            icon = Icons.Filled.LocalFireDepartment,
                            accentColor = WarningAmber,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Weekly Performance Bar Chart
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = Color(0x18FFFFFF)
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
                            Text(
                                text = "Weekly Performance",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Avg 84%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CyanAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Custom Vector Bar Chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val barWidth = 24.dp.toPx()
                                val totalBars = analytics.weeklyScores.size
                                val stepX = size.width / totalBars
                                val maxHeight = size.height - 24.dp.toPx()

                                analytics.weeklyScores.forEachIndexed { index, scorePoint ->
                                    val barHeight = maxHeight * scorePoint.score * chartAnimFraction
                                    val barX = index * stepX + (stepX - barWidth) / 2
                                    val barY = maxHeight - barHeight

                                    // Gradient Bar
                                    drawRoundRect(
                                        brush = Brush.verticalGradient(
                                            listOf(BrightCyan, ElectricBlue, DeepViolet)
                                        ),
                                        topLeft = Offset(barX, barY),
                                        size = Size(barWidth, barHeight),
                                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Day Labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            analytics.weeklyScores.forEach { pt ->
                                Text(
                                    text = pt.day,
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Subject Performance Meters
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = Color(0x18FFFFFF)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Subject Performance",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        analytics.subjectProficiency.forEach { subj ->
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = subj.subject,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${subj.scorePercent}%",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (subj.scorePercent >= 85) BrightCyan else NeonViolet
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { (subj.scorePercent / 100f) * chartAnimFraction },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (subj.scorePercent >= 85) CyanAccent else NeonViolet,
                                    trackColor = Color(0x20FFFFFF)
                                )
                            }
                        }
                    }
                }
            }

            // AI Analysis Glass Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0x22131F3B),
                    borderColor = CyanAccent.copy(alpha = 0.45f),
                    isActive = true
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
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
                                    tint = BrightCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI DIAGNOSTIC INSIGHT",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrightCyan,
                                    letterSpacing = 1.sp
                                )
                            }

                            AiPulsingOrb(size = 36.dp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "• Strongest subject: Java (92%)\n• Weakest area: Memory Management (78%)\n• Recommended daily study time: 30 minutes",
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1A00F2FE))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "💡 Tip: Completing 1 quick quiz before bed increases 7-day retention by up to 34%.",
                                fontSize = 12.sp,
                                color = CyanAccent,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsMetricCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x18FFFFFF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}
