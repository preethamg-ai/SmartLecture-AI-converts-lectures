package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
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
import com.example.model.SearchCategory
import com.example.model.SearchResultItem
import com.example.service.SmartLectureService
import com.example.ui.components.GlassCard
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onNavigateLecture: (lectureId: String) -> Unit,
    onNavigateNotes: (lectureId: String) -> Unit,
    onNavigateQuiz: (lectureId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val service = SmartLectureService.instance
    var query by remember { mutableStateOf("memory management") }
    val results = remember(query) { service.searchAll(query) }

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_global_search")
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Search Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("search_back_button")
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

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search lectures, notes, questions...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = CyanAccent
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear", tint = TextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("global_search_input_field"),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x1AFFFFFF),
                        unfocusedContainerColor = Color(0x12FFFFFF),
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = GlassBorderLight,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips quick suggestions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickSearchTag("Memory Management") { query = "Memory Management" }
                QuickSearchTag("Deadlock") { query = "Deadlock" }
                QuickSearchTag("Java") { query = "Java" }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Search Results Count
            Text(
                text = if (query.isBlank()) "POPULAR DISCOVERIES" else "FOUND ${results.size} MATCHING RESULTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanAccent,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Results List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(results) { item ->
                    SearchResultCard(
                        item = item,
                        onClick = {
                            when (item.category) {
                                SearchCategory.LECTURE -> onNavigateLecture(item.targetId)
                                SearchCategory.NOTE -> onNavigateNotes(item.targetId.removePrefix("note_"))
                                SearchCategory.QUESTION -> onNavigateQuiz(item.targetId)
                                SearchCategory.TOPIC -> onNavigateLecture(item.targetId)
                            }
                        }
                    )
                }

                if (results.isEmpty() && query.isNotBlank()) {
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "🔍", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No direct match found for '$query'",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Try searching for broader terms like 'Operating Systems', 'Java', or 'Concurrency'",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickSearchTag(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x16FFFFFF))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun SearchResultCard(
    item: SearchResultItem,
    onClick: () -> Unit
) {
    val categoryIcon = when (item.category) {
        SearchCategory.LECTURE -> Icons.Filled.School
        SearchCategory.NOTE -> Icons.Filled.Description
        SearchCategory.QUESTION -> Icons.Filled.Psychology
        SearchCategory.TOPIC -> Icons.Filled.Tag
    }

    val categoryColor = when (item.category) {
        SearchCategory.LECTURE -> BrightCyan
        SearchCategory.NOTE -> NeonViolet
        SearchCategory.QUESTION -> ElectricBlueLight
        SearchCategory.TOPIC -> WarningAmber
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x18FFFFFF),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(categoryColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = item.category.name,
                    tint = categoryColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
