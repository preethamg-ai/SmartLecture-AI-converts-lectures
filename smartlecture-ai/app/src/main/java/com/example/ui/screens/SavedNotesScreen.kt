package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
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
import com.example.model.NoteDocument
import com.example.service.SmartLectureService
import com.example.ui.components.GlassCard
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

enum class NoteFilter {
    ALL, RECENT, FAVORITES
}

@Composable
fun SavedNotesScreen(
    onNavigateNoteDetail: (lectureId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val service = SmartLectureService.instance
    val notes by service.notes.collectAsState()
    val lectures by service.lectures.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(NoteFilter.ALL) }

    // Combined note items
    val filteredNotes = remember(notes, lectures, searchQuery, selectedFilter) {
        val allNotes = notes + lectures.filter { lec ->
            notes.none { it.lectureId == lec.id }
        }.map { lec ->
            NoteDocument(
                id = "note_${lec.id}",
                lectureId = lec.id,
                title = lec.title,
                subject = lec.subject,
                lastEdited = lec.date,
                summary = lec.summary,
                overview = "Lecture overview for ${lec.title}",
                keyConcepts = listOf("Core Architecture", "Fundamental Principles"),
                importantPoints = listOf("Review high-yield exam sections"),
                definitions = emptyList(),
                examples = emptyList(),
                examTopics = listOf("Exam Review Section"),
                faqs = emptyList(),
                pageCount = 6,
                isFavorite = lec.isFavorite
            )
        }

        allNotes.filter { note ->
            val matchesQuery = searchQuery.isBlank() ||
                    note.title.contains(searchQuery, ignoreCase = true) ||
                    note.subject.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                NoteFilter.ALL -> true
                NoteFilter.RECENT -> note.lastEdited.contains("Today") || note.lastEdited.contains("Yesterday")
                NoteFilter.FAVORITES -> note.isFavorite
            }

            matchesQuery && matchesFilter
        }
    }

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_saved_notes")
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Saved Notes Library",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${filteredNotes.size} smart syntheses in your knowledge bank",
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
                        imageVector = Icons.Filled.CollectionsBookmark,
                        contentDescription = "Library",
                        tint = BrightCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search notes...", color = TextMuted, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = CyanAccent
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("library_search_input"),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x18FFFFFF),
                    unfocusedContainerColor = Color(0x10FFFFFF),
                    focusedBorderColor = CyanAccent,
                    unfocusedBorderColor = GlassBorderLight,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTabChip(
                    label = "All",
                    isSelected = selectedFilter == NoteFilter.ALL,
                    onClick = { selectedFilter = NoteFilter.ALL },
                    testTag = "filter_all"
                )
                FilterTabChip(
                    label = "Recent",
                    isSelected = selectedFilter == NoteFilter.RECENT,
                    onClick = { selectedFilter = NoteFilter.RECENT },
                    testTag = "filter_recent"
                )
                FilterTabChip(
                    label = "Favorites ⭐",
                    isSelected = selectedFilter == NoteFilter.FAVORITES,
                    onClick = { selectedFilter = NoteFilter.FAVORITES },
                    testTag = "filter_favorites"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Note Cards List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(filteredNotes) { note ->
                    SavedNoteCard(
                        note = note,
                        onClick = { onNavigateNoteDetail(note.lectureId) },
                        onToggleFavorite = {
                            service.saveNote(note.id, !note.isFavorite)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) CyanAccent else Color(0x16FFFFFF))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) BgDeepDark else Color.White
        )
    }
}

@Composable
private fun SavedNoteCard(
    note: NoteDocument,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0x18FFFFFF),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x2000F2FE))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = note.subject,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = note.lastEdited,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = note.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${note.pageCount} pages • ${note.keyConcepts.size} key concepts",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x12FFFFFF))
            ) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = "Favorite",
                    tint = if (note.isFavorite) WarningAmber else TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
