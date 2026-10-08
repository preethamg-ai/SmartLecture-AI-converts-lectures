package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Lecture
import com.example.service.SmartLectureService
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun LectureDetailsScreen(
    lectureId: String,
    onBack: () -> Unit,
    onNavigateNotes: (lectureId: String) -> Unit,
    onStartQuiz: (lectureId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val service = SmartLectureService.instance
    val lectures by service.lectures.collectAsState()
    val lecture = lectures.find { it.id == lectureId } ?: lectures.first()

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_lecture_details")
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("lecture_details_back")
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x18FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Lecture Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Main Hero Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0x22131F3B),
                borderColor = CyanAccent.copy(alpha = 0.4f),
                isActive = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2500F2FE))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = lecture.subject,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        }

                        Text(
                            text = lecture.date,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = lecture.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = "Duration",
                                tint = BrightCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = lecture.duration, fontSize = 12.sp, color = TextSecondary)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.InsertDriveFile,
                                contentDescription = "Format",
                                tint = NeonViolet,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${lecture.fileType} (${lecture.fileSize})", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }

            // AI Generated Summary
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "AI",
                            tint = BrightCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI-GENERATED SUMMARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightCyan,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = lecture.summary,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 21.sp
                    )
                }
            }

            // Key Metrics Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Number of concepts detected
                GlassCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = Color(0x18FFFFFF)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${lecture.conceptsCount}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightCyan
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Concepts Detected",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Number of important topics
                GlassCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = Color(0x18FFFFFF)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${lecture.topicsCount}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonViolet
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Important Topics",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Quiz Availability Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color(0x1A00F2FE),
                borderColor = CyanAccent.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "Quiz",
                            tint = CyanAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Quiz Assessment Ready",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "5 high-yield multiple choice questions",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Available ✓",
                            fontSize = 11.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassPrimaryButton(
                    text = "Start Quiz →",
                    onClick = { onStartQuiz(lectureId) },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "lecture_details_start_quiz_btn"
                )

                GlassSecondaryButton(
                    text = "Read Full AI Smart Notes",
                    onClick = { onNavigateNotes(lectureId) },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "lecture_details_view_notes_btn"
                )
            }
        }
    }
}
