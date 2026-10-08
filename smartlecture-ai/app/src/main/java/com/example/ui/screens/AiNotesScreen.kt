package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NoteDocument
import com.example.service.SmartLectureService
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun AiNotesScreen(
    lectureId: String,
    onBack: () -> Unit,
    onNavigateQuiz: (lectureId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val service = SmartLectureService.instance
    val notesList by service.notes.collectAsState()

    val currentNote: NoteDocument = notesList.find { it.lectureId == lectureId }
        ?: notesList.firstOrNull()
        ?: NoteDocument(
            id = "note_default",
            lectureId = lectureId,
            title = "Operating Systems",
            subject = "Computer Science",
            lastEdited = "Today",
            summary = "Operating systems manage computer hardware and software resources, providing common services for computer programs.",
            overview = "The operating system acts as an intermediary between the user of a computer and computer hardware.",
            keyConcepts = listOf("Process Management", "Memory Hierarchy", "Concurrency Control"),
            importantPoints = listOf("Context switches incur CPU overhead", "Deadlock requires Coffman criteria"),
            definitions = emptyList(),
            examples = emptyList(),
            examTopics = emptyList(),
            faqs = emptyList()
        )

    var isFavorite by remember(currentNote.id) { mutableStateOf(currentNote.isFavorite) }

    LiquidGlassBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_ai_notes"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Top Navigation & Action Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .testTag("notes_back_button")
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

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Save Star
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                service.saveNote(currentNote.id, isFavorite)
                                Toast.makeText(
                                    context,
                                    if (isFavorite) "Saved to Favorites ⭐" else "Removed from Favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .testTag("notes_save_button")
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0x18FFFFFF))
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Save",
                                tint = if (isFavorite) WarningAmber else Color.White
                            )
                        }

                        // Copy Action
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Notes copied to clipboard 📋", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .testTag("notes_copy_button")
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0x18FFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.White
                            )
                        }

                        // Export Action
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Exporting Smart Notes as PDF ⬇", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .testTag("notes_export_button")
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0x18FFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Download,
                                contentDescription = "Export",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Header Title
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2000F2FE))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentNote.subject,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentNote.lastEdited,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentNote.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // AI Summary Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = Color(0x22131F3B),
                    borderColor = CyanAccent.copy(alpha = 0.45f),
                    isActive = true
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = "AI Summary",
                                tint = BrightCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI SUMMARY",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrightCyan,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentNote.summary,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Overview Section
            item {
                SectionCard(
                    title = "Overview",
                    icon = Icons.Filled.Info,
                    iconTint = ElectricBlueLight
                ) {
                    Text(
                        text = currentNote.overview,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 21.sp
                    )
                }
            }

            // Key Concepts Section
            item {
                SectionCard(
                    title = "Key Concepts",
                    icon = Icons.Filled.Lightbulb,
                    iconTint = NeonViolet
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentNote.keyConcepts.forEach { concept ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "•", color = CyanAccent, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                                Text(text = concept, color = TextPrimary, fontSize = 13.sp, lineHeight = 19.sp)
                            }
                        }
                    }
                }
            }

            // Important Points Section
            item {
                SectionCard(
                    title = "Important Points",
                    icon = Icons.Filled.PriorityHigh,
                    iconTint = WarningAmber
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentNote.importantPoints.forEach { pt ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "✓", color = SuccessGreen, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                                Text(text = pt, color = TextSecondary, fontSize = 13.sp, lineHeight = 19.sp)
                            }
                        }
                    }
                }
            }

            // Definitions Section
            item {
                SectionCard(
                    title = "Definitions",
                    icon = Icons.Filled.MenuBook,
                    iconTint = BrightCyan
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        currentNote.definitions.forEach { def ->
                            Column {
                                Text(
                                    text = def.term,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = def.definition,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // Examples Section
            item {
                SectionCard(
                    title = "Examples",
                    icon = Icons.Filled.Code,
                    iconTint = NeonPink
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentNote.examples.forEach { ex ->
                            Text(
                                text = ex,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            // Exam Important Topics Section
            item {
                SectionCard(
                    title = "Exam Important Topics",
                    icon = Icons.Filled.AssignmentLate,
                    iconTint = NeonViolet
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentNote.examTopics.forEach { topic ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x187C3AED))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "🔥 $topic",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Frequently Asked Questions Section
            item {
                SectionCard(
                    title = "Frequently Asked Questions",
                    icon = Icons.Filled.HelpOutline,
                    iconTint = CyanAccent
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        currentNote.faqs.forEach { faq ->
                            Column {
                                Text(
                                    text = "Q: ${faq.question}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "A: ${faq.answer}",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom CTA: Generate Quiz
            item {
                GlassPrimaryButton(
                    text = "📝 Generate Quiz",
                    onClick = { onNavigateQuiz(lectureId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    testTag = "notes_generate_quiz_cta"
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0x16FFFFFF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            content()
        }
    }
}
