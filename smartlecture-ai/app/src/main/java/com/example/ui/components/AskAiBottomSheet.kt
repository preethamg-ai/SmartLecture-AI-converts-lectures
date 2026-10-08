package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
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
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskAiBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialQuery: String = ""
) {
    val coroutineScope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf(initialQuery) }
    var isLoading by remember { mutableStateOf(false) }
    var aiResponse by remember { mutableStateOf<String?>(null) }
    var chatHistory by remember {
        mutableStateOf(
            listOf(
                "AI" to "Hello Alex! I'm SmartLecture AI. Ask me anything about your lectures, request simplified breakdowns, or generate instant revision questions."
            )
        )
    }

    val quickPrompts = listOf(
        "Explain this topic simply.",
        "Create 5 questions.",
        "Summarize this lecture.",
        "What should I study next?"
    )

    fun sendPrompt(prompt: String) {
        if (prompt.isBlank() || isLoading) return
        val userMsg = prompt
        inputText = ""
        chatHistory = chatHistory + ("User" to userMsg)
        isLoading = true

        coroutineScope.launch {
            val response = SmartLectureService.instance.askAI(userMsg)
            chatHistory = chatHistory + ("AI" to response)
            aiResponse = response
            isLoading = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BgMidnight.copy(alpha = 0.95f),
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.3f))
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(CyanAccent, NeonViolet))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "AI",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Ask SmartLecture AI",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Instant answers & smart summaries",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("ai_sheet_close")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Prompt Chips
            Text(
                text = "SUGGESTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickPrompts.take(2).forEach { chip ->
                    SuggestionChip(
                        onClick = { sendPrompt(chip) },
                        label = { Text(chip, fontSize = 12.sp, color = Color.White) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0x22FFFFFF)
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = GlassBorderLight
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickPrompts.drop(2).forEach { chip ->
                    SuggestionChip(
                        onClick = { sendPrompt(chip) },
                        label = { Text(chip, fontSize = 12.sp, color = Color.White) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0x22FFFFFF)
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = GlassBorderLight
                        )
                    )
                }
            }

            // Chat Messages Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp, max = 260.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x18FFFFFF))
                    .padding(12.dp)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    chatHistory.forEach { (sender, msg) ->
                        val isUser = sender == "User"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = 280.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 14.dp,
                                            topEnd = 14.dp,
                                            bottomStart = if (isUser) 14.dp else 4.dp,
                                            bottomEnd = if (isUser) 4.dp else 14.dp
                                        )
                                    )
                                    .background(
                                        if (isUser) Brush.linearGradient(
                                            listOf(ElectricBlueLight, NeonViolet)
                                        ) else Brush.linearGradient(
                                            listOf(Color(0x3500F2FE), Color(0x257C3AED))
                                        )
                                    )
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = msg,
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = CyanAccent,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "SmartLecture AI is formulating response...",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask anything about your lectures...", fontSize = 13.sp, color = TextMuted) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_input_text_field"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x18FFFFFF),
                        unfocusedContainerColor = Color(0x10FFFFFF),
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = GlassBorderLight,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { sendPrompt(inputText) },
                    enabled = inputText.isNotBlank() && !isLoading,
                    modifier = Modifier
                        .testTag("ai_send_button")
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (inputText.isNotBlank()) Brush.linearGradient(
                                listOf(CyanAccent, ElectricBlue)
                            ) else Brush.linearGradient(listOf(Color(0x22FFFFFF), Color(0x22FFFFFF)))
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) Color.White else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
