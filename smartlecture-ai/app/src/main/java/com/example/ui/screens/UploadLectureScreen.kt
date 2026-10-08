package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.service.SmartLectureService
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SelectedFileMeta(
    val name: String,
    val type: String,
    val size: String,
    val subject: String
)

@Composable
fun UploadLectureScreen(
    onBack: () -> Unit,
    onStartProcessing: (lectureId: String, generateQuiz: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedFile by remember {
        mutableStateOf<SelectedFileMeta?>(
            SelectedFileMeta(
                name = "CS301_Distributed_Systems_Lecture_04.pdf",
                type = "PDF",
                size = "6.4 MB",
                subject = "Distributed Computing"
            )
        )
    }

    var uploadProgress by remember { mutableFloatStateOf(1.0f) }
    var isUploading by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var recordSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1000)
                recordSeconds++
            }
        }
    }

    fun selectPresetFile(name: String, type: String, size: String, subject: String) {
        isUploading = true
        uploadProgress = 0.1f
        coroutineScope.launch {
            for (p in 2..10) {
                delay(100)
                uploadProgress = p / 10f
            }
            selectedFile = SelectedFileMeta(name, type, size, subject)
            isUploading = false
        }
    }

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_upload_lecture")
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("upload_back_button")
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

                Column {
                    Text(
                        text = "Upload Lecture",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Give your lecture to AI and turn it into knowledge.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Large Glass Upload Drop Area
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0x18FFFFFF),
                borderColor = CyanAccent.copy(alpha = 0.35f),
                isActive = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0x3000F2FE),
                                        Color(0x207C3AED)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Description,
                            contentDescription = "Document",
                            tint = CyanAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Drop your lecture here",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "PDF • DOCX • TXT • MP3 • WAV",
                        fontSize = 12.sp,
                        color = CyanAccent,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        GlassPrimaryButton(
                            text = "Browse Files",
                            onClick = {
                                selectPresetFile(
                                    "Operating_Systems_Concurrency_Ch4.pdf",
                                    "PDF",
                                    "4.8 MB",
                                    "Computer Science"
                                )
                            },
                            testTag = "upload_browse_button"
                        )

                        GlassSecondaryButton(
                            text = if (isRecording) "Stop (${recordSeconds}s)" else "Record Lecture",
                            onClick = {
                                if (isRecording) {
                                    isRecording = false
                                    selectedFile = SelectedFileMeta(
                                        "Live_Class_Recording_${recordSeconds}s.wav",
                                        "WAV",
                                        "8.2 MB",
                                        "Audio Lecture"
                                    )
                                } else {
                                    recordSeconds = 0
                                    isRecording = true
                                }
                            },
                            testTag = "upload_record_button",
                            icon = {
                                Icon(
                                    imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                                    contentDescription = "Record",
                                    tint = if (isRecording) NeonPink else BrightCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Quick Samples selector
            Column {
                Text(
                    text = "OR CHOOSE FROM SAMPLE LECTURES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SampleChip(
                        name = "AI Networks (PDF)",
                        onClick = { selectPresetFile("Deep_Learning_Lecture_06.pdf", "PDF", "5.1 MB", "Machine Learning") }
                    )
                    SampleChip(
                        name = "Java Concurrency (Audio)",
                        onClick = { selectPresetFile("Java_Threading_Hall_Audio.mp3", "MP3", "14.2 MB", "Java SE") }
                    )
                }
            }

            // Selected File Details Card
            AnimatedVisibility(visible = selectedFile != null) {
                selectedFile?.let { file ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = Color(0x22131F3A),
                        borderColor = BrightCyan.copy(alpha = 0.4f),
                        isActive = true
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x2500F2FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (file.type == "MP3" || file.type == "WAV") Icons.Filled.AudioFile else Icons.Filled.InsertDriveFile,
                                        contentDescription = "File",
                                        tint = CyanAccent,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = file.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${file.type} • ${file.size} • ${file.subject}",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Upload Progress Indicator
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (uploadProgress >= 1f) "Upload complete" else "Uploading...",
                                    fontSize = 11.sp,
                                    color = if (uploadProgress >= 1f) SuccessGreen else TextSecondary
                                )
                                Text(
                                    text = "${(uploadProgress * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { uploadProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CyanAccent,
                                trackColor = Color(0x22FFFFFF)
                            )
                        }
                    }
                }
            }

            // Action Generation Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassPrimaryButton(
                    text = "✨ Generate Notes + Quiz",
                    onClick = {
                        val file = selectedFile
                        coroutineScope.launch {
                            val newLecture = SmartLectureService.instance.uploadLecture(
                                fileName = file?.name ?: "Lecture_01.pdf",
                                fileType = file?.type ?: "PDF",
                                fileSize = file?.size ?: "4.5 MB",
                                subject = file?.subject ?: "Computer Science"
                            )
                            onStartProcessing(newLecture.id, true)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "upload_generate_both_button"
                )

                GlassSecondaryButton(
                    text = "✨ Generate Notes Only",
                    onClick = {
                        val file = selectedFile
                        coroutineScope.launch {
                            val newLecture = SmartLectureService.instance.uploadLecture(
                                fileName = file?.name ?: "Lecture_01.pdf",
                                fileType = file?.type ?: "PDF",
                                fileSize = file?.size ?: "4.5 MB",
                                subject = file?.subject ?: "Computer Science"
                            )
                            onStartProcessing(newLecture.id, false)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "upload_generate_notes_only_button"
                )
            }
        }
    }
}

@Composable
private fun SampleChip(
    name: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x18FFFFFF))
            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = name, fontSize = 12.sp, color = TextSecondary)
    }
}
