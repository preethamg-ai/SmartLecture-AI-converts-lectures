package com.example.ui.screens

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

data class QuickActionItem(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color,
    val actionType: String
)

@Composable
fun HomeScreen(
    onNavigateUpload: () -> Unit,
    onNavigateNotes: (String) -> Unit,
    onNavigateQuiz: (String) -> Unit,
    onNavigateLectureDetail: (String) -> Unit,
    onNavigateLibrary: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateSearch: () -> Unit,
    onOpenAiSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val service = SmartLectureService.instance
    val lectures by service.lectures.collectAsState()
    val profile by service.userProfile.collectAsState()
    val analytics by service.analytics.collectAsState()

    // Animated Statistics Counters
    var startCountAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startCountAnimation = true
    }

    val animatedLectures by animateIntAsState(
        targetValue = if (startCountAnimation) 24 else 0,
        animationSpec = tween(durationMillis = 1000),
        label = "CountLectures"
    )
    val animatedNotes by animateIntAsState(
        targetValue = if (startCountAnimation) 86 else 0,
        animationSpec = tween(durationMillis = 1000),
        label = "CountNotes"
    )
    val animatedQuizzes by animateIntAsState(
        targetValue = if (startCountAnimation) 31 else 0,
        animationSpec = tween(durationMillis = 1000),
        label = "CountQuizzes"
    )
    val animatedAvgScore by animateIntAsState(
        targetValue = if (startCountAnimation) 84 else 0,
        animationSpec = tween(durationMillis = 1000),
        label = "CountAvgScore"
    )

    val quickActions = listOf(
        QuickActionItem("Upload Lecture", "PDF • DOCX • Audio", Icons.Filled.CloudUpload, CyanAccent, "upload"),
        QuickActionItem("Generate Notes", "Instant AI synthesis", Icons.Filled.EditNote, NeonViolet, "notes"),
        QuickActionItem("Generate Quiz", "Exam ready practice", Icons.Filled.Psychology, ElectricBlueLight, "quiz"),
        QuickActionItem("Record Lecture", "Live voice to text", Icons.Filled.Mic, BrightCyan, "record")
    )

    LiquidGlassBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_home"),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Top Bar: Greeting & Profile Avatar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Good Morning",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "👋", fontSize = 20.sp)
                        }
                        Text(
                            text = "Ready to learn smarter?",
                            fontSize = 14.sp,
                            color = CyanAccent
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateSearch,
                            modifier = Modifier
                                .testTag("home_search_button")
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0x18FFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Profile Avatar
                        Box(
                            modifier = Modifier
                                .testTag("home_profile_avatar")
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CyanAccent, NeonViolet)
                                    )
                                )
                                .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                .clickable { onNavigateProfile() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AM",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Large AI Study Assistant Glass Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = Color(0x22FFFFFF),
                    borderColor = CyanAccent.copy(alpha = 0.45f),
                    isActive = true
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = "AI Assistant",
                                    tint = BrightCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "YOUR AI STUDY ASSISTANT",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrightCyan,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Upload a lecture and I'll turn it into notes and a personalized quiz.",
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            GlassPrimaryButton(
                                text = "✨ Start Learning",
                                onClick = onNavigateUpload,
                                testTag = "home_start_learning_button"
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Pulsing AI Orb
                        AiPulsingOrb(size = 90.dp)
                    }
                }
            }

            // Learning Statistics Horizontal Cards with Animated Numbers
            item {
                Column {
                    Text(
                        text = "LEARNING STATISTICS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            label = "Lectures",
                            value = "$animatedLectures",
                            color = BrightCyan,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            label = "Notes",
                            value = "$animatedNotes",
                            color = NeonViolet,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            label = "Quizzes",
                            value = "$animatedQuizzes",
                            color = ElectricBlueLight,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            label = "Avg Score",
                            value = "$animatedAvgScore%",
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Actions Grid (2x2)
            item {
                Column {
                    Text(
                        text = "QUICK ACTIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            quickActions.take(2).forEach { action ->
                                QuickActionCard(
                                    action = action,
                                    onClick = {
                                        when (action.actionType) {
                                            "upload", "record" -> onNavigateUpload()
                                            "notes" -> onNavigateNotes("lec_1")
                                            "quiz" -> onNavigateQuiz("lec_1")
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            quickActions.drop(2).forEach { action ->
                                QuickActionCard(
                                    action = action,
                                    onClick = {
                                        when (action.actionType) {
                                            "upload", "record" -> onNavigateUpload()
                                            "notes" -> onNavigateNotes("lec_1")
                                            "quiz" -> onNavigateQuiz("lec_1")
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Recent Lectures Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Lectures",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    TextButton(
                        onClick = onNavigateLibrary,
                        modifier = Modifier.testTag("home_view_all_lectures")
                    ) {
                        Text(
                            text = "View All →",
                            fontSize = 13.sp,
                            color = CyanAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Recent Lecture Items
            items(lectures.take(3)) { lecture ->
                LectureItemCard(
                    lecture = lecture,
                    onClick = { onNavigateLectureDetail(lecture.id) },
                    onNotesClick = { onNavigateNotes(lecture.id) },
                    onQuizClick = { onNavigateQuiz(lecture.id) }
                )
            }

            // AI Learning Insight Glass Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0x22172554),
                    borderColor = NeonViolet.copy(alpha = 0.5f),
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
                                Text(text = "🧠", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI Learning Insight",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonViolet.copy(alpha = 0.25f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+12% Acc",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrightCyan
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "You are improving in Java and Data Structures. Your quiz accuracy increased by 12% this week. I recommend revising Linked Lists next.",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlassPrimaryButton(
                                text = "View Recommendations",
                                onClick = onOpenAiSheet,
                                testTag = "home_view_recommendations_button"
                            )

                            // Decorative mini orb
                            AiPulsingOrb(size = 46.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMetricCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x18FFFFFF),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    action: QuickActionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.height(108.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0x18FFFFFF),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(action.iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = action.iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = action.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = action.description,
                    fontSize = 10.sp,
                    color = TextMuted,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun LectureItemCard(
    lecture: Lecture,
    onClick: () -> Unit,
    onNotesClick: () -> Unit,
    onQuizClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0x1AFFFFFF),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lecture.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${lecture.date} • ${lecture.duration} • ${lecture.subject}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                // File type badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2000F2FE))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = lecture.fileType,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress status chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Notes status chip
                    StatusChip(
                        label = "Notes",
                        isCompleted = lecture.notesGenerated,
                        onClick = onNotesClick
                    )

                    // Quiz status chip
                    StatusChip(
                        label = "Quiz",
                        isCompleted = lecture.quizGenerated,
                        onClick = onQuizClick
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Details",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    label: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isCompleted) SuccessGreen.copy(alpha = 0.18f) else WarningAmber.copy(alpha = 0.18f)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isCompleted) "$label ✓" else "$label pending",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isCompleted) SuccessGreen else WarningAmber
            )
        }
    }
}
