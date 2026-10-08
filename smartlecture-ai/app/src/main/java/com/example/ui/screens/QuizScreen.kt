package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Quiz
import com.example.model.QuizResult
import com.example.service.SmartLectureService
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    lectureId: String,
    onBack: () -> Unit,
    onQuizSubmitted: (QuizResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val service = SmartLectureService.instance
    val quizzes by service.quizzes.collectAsState()

    val quiz: Quiz = quizzes[lectureId]
        ?: quizzes.values.firstOrNull()
        ?: Quiz(
            id = "quiz_default",
            lectureId = lectureId,
            title = "Operating Systems Assessment",
            subject = "Computer Science",
            questions = emptyList()
        )

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    var isSubmitting by remember { mutableStateOf(false) }

    val currentQuestion = quiz.questions.getOrNull(currentQuestionIndex)
    val totalQuestions = quiz.questions.size
    val progress = if (totalQuestions > 0) (currentQuestionIndex + 1).toFloat() / totalQuestions else 0f

    val optionLetters = listOf("A", "B", "C", "D")

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp)
                .testTag("screen_quiz")
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Top Bar with Subject & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = quiz.subject,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = quiz.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .testTag("quiz_close_button")
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

                Spacer(modifier = Modifier.height(18.dp))

                // Question Counter & Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${currentQuestionIndex + 1} of $totalQuestions",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = CyanAccent,
                    trackColor = Color(0x22FFFFFF)
                )

                Spacer(modifier = Modifier.height(26.dp))

                // Question Card
                if (currentQuestion != null) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = Color(0x20FFFFFF),
                        borderColor = CyanAccent.copy(alpha = 0.35f),
                        isActive = true
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x257C3AED))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = currentQuestion.topic,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrightCyan
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentQuestion.question,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 24.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Glass Answer Option Cards (A, B, C, D)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        currentQuestion.options.forEachIndexed { optIndex, optionText ->
                            val isSelected = userAnswers[currentQuestionIndex] == optIndex
                            val letter = optionLetters.getOrElse(optIndex) { "${optIndex + 1}" }

                            AnswerOptionCard(
                                letter = letter,
                                text = optionText,
                                isSelected = isSelected,
                                onClick = {
                                    userAnswers[currentQuestionIndex] = optIndex
                                },
                                testTag = "quiz_option_$optIndex"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Navigation Buttons (Prev / Next / Submit)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentQuestionIndex > 0) {
                    GlassSecondaryButton(
                        text = "Previous",
                        onClick = { currentQuestionIndex-- },
                        modifier = Modifier.weight(1f),
                        testTag = "quiz_prev_button",
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Prev",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }

                if (currentQuestionIndex < totalQuestions - 1) {
                    GlassPrimaryButton(
                        text = "Next",
                        onClick = { currentQuestionIndex++ },
                        modifier = Modifier.weight(if (currentQuestionIndex > 0) 1f else 2f),
                        testTag = "quiz_next_button",
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                } else {
                    GlassPrimaryButton(
                        text = "Submit Quiz",
                        onClick = {
                            isSubmitting = true
                            coroutineScope.launch {
                                val result = service.submitQuiz(quiz.id, userAnswers)
                                isSubmitting = false
                                onQuizSubmitted(result)
                            }
                        },
                        isLoading = isSubmitting,
                        modifier = Modifier.weight(if (currentQuestionIndex > 0) 1f else 2f),
                        testTag = "quiz_submit_button"
                    )
                }
            }
        }
    }
}

@Composable
private fun AnswerOptionCard(
    letter: String,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(150),
        label = "OptionScale"
    )

    Box(
        modifier = Modifier
            .testTag(testTag)
            .scale(scale)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isSelected) Brush.linearGradient(
                    listOf(
                        Color(0x3500F2FE),
                        Color(0x287C3AED)
                    )
                ) else Brush.linearGradient(
                    listOf(
                        Color(0x18FFFFFF),
                        Color(0x0EFFFFFF)
                    )
                )
            )
            .border(
                BorderStroke(
                    if (isSelected) 1.5.dp else 1.dp,
                    if (isSelected) CyanAccent else Color(0x25FFFFFF)
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) CyanAccent else Color(0x20FFFFFF)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) BgDeepDark else Color.White
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextSecondary,
                lineHeight = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
