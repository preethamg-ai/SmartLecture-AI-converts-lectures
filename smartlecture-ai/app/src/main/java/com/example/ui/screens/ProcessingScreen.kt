package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SmartLectureService
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

enum class StageStatus {
    DONE, CURRENT, PENDING
}

data class ProcessingStage(
    val title: String,
    val status: StageStatus
)

@Composable
fun ProcessingScreen(
    lectureId: String,
    generateQuiz: Boolean,
    onProcessingFinished: (lectureId: String, openQuiz: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var stageStep by remember { mutableIntStateOf(0) }
    var rawProgress by remember { mutableFloatStateOf(0.15f) }

    val animatedProgress by animateFloatAsState(
        targetValue = rawProgress,
        animationSpec = tween(600),
        label = "ProcessingProgress"
    )

    LaunchedEffect(Unit) {
        // Trigger background generation in SmartLectureService
        SmartLectureService.instance.generateNotes(lectureId)
        if (generateQuiz) {
            SmartLectureService.instance.generateQuiz(lectureId)
        }

        // Animated stage simulation
        delay(900)
        stageStep = 1
        rawProgress = 0.45f

        delay(1000)
        stageStep = 2
        rawProgress = 0.75f

        delay(1100)
        stageStep = 3
        rawProgress = 1.0f

        delay(900)
        onProcessingFinished(lectureId, generateQuiz)
    }

    val stages = listOf(
        ProcessingStage("Reading lecture", if (stageStep > 0) StageStatus.DONE else if (stageStep == 0) StageStatus.CURRENT else StageStatus.PENDING),
        ProcessingStage("Extracting concepts", if (stageStep > 1) StageStatus.DONE else if (stageStep == 1) StageStatus.CURRENT else StageStatus.PENDING),
        ProcessingStage("Creating notes", if (stageStep > 2) StageStatus.DONE else if (stageStep == 2) StageStatus.CURRENT else StageStatus.PENDING),
        ProcessingStage("Generating quiz", if (stageStep > 3) StageStatus.DONE else if (stageStep == 3) StageStatus.CURRENT else StageStatus.PENDING)
    )

    LiquidGlassBackground(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp)
                .testTag("screen_ai_processing"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Large Glowing AI Orb
                AiPulsingOrb(size = 180.dp)

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "AI is understanding your lecture...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Synthesizing audio & document tokens with Gemini core",
                    fontSize = 13.sp,
                    color = CyanAccent
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyanAccent,
                    trackColor = Color(0x22FFFFFF)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Stages Glass Container
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = Color(0x1AFFFFFF),
                    borderColor = CyanAccent.copy(alpha = 0.25f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        stages.forEach { stage ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                when (stage.status) {
                                    StageStatus.DONE -> {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(SuccessGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Done",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    StageStatus.CURRENT -> {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(CyanAccent),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                            )
                                        }
                                    }
                                    StageStatus.PENDING -> {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x30FFFFFF))
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    text = stage.title,
                                    fontSize = 14.sp,
                                    fontWeight = if (stage.status == StageStatus.CURRENT) FontWeight.Bold else FontWeight.Medium,
                                    color = when (stage.status) {
                                        StageStatus.DONE -> SuccessGreen
                                        StageStatus.CURRENT -> Color.White
                                        StageStatus.PENDING -> TextMuted
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
