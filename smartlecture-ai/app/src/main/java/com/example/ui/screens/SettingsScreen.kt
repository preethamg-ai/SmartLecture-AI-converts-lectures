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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var darkModeEnabled by remember { mutableStateOf(true) }
    var glassIntensity by remember { mutableFloatStateOf(0.85f) }
    var animationsEnabled by remember { mutableStateOf(true) }
    var aiDetailLevel by remember { mutableStateOf("Detailed Analysis") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English (US)") }

    LiquidGlassBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_settings")
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .testTag("settings_back_button")
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
                    text = "Settings & Preferences",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Appearance Section
            SettingsSectionHeader(title = "APPEARANCE & LIQUID GLASS")
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
                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Dark Mode", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("Futuristic midnight liquid glass theme", color = TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CyanAccent
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0x15FFFFFF))

                    // Glass Intensity Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Glass Intensity", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("${(glassIntensity * 100).toInt()}%", color = CyanAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Adjust backdrop refraction & translucency", color = TextMuted, fontSize = 11.sp)
                        Slider(
                            value = glassIntensity,
                            onValueChange = { glassIntensity = it },
                            colors = SliderDefaults.colors(
                                thumbColor = CyanAccent,
                                activeTrackColor = CyanAccent,
                                inactiveTrackColor = Color(0x25FFFFFF)
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0x15FFFFFF))

                    // Animation Settings Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Animation Settings", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("Fluid orbs & micro-interaction springs", color = TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = animationsEnabled,
                            onCheckedChange = { animationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = NeonViolet
                            )
                        )
                    }
                }
            }

            // AI Preferences Section
            SettingsSectionHeader(title = "AI PREFERENCES")
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Synthesis Depth", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text(aiDetailLevel, color = CyanAccent, fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                aiDetailLevel = if (aiDetailLevel == "Detailed Analysis") "Concise Bullet Points" else "Detailed Analysis"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF))
                        ) {
                            Text("Toggle", color = Color.White, fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider(color = Color(0x15FFFFFF))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Quiz Difficulty", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("Adaptive based on past accuracy", color = TextMuted, fontSize = 11.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2500F2FE))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Adaptive", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Notifications & Language
            SettingsSectionHeader(title = "PREFERENCES & SYSTEM")
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Study Reminders", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("Streak & quiz revision notifications", color = TextMuted, fontSize = 11.sp)
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SuccessGreen
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0x15FFFFFF))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Language", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text(selectedLanguage, color = TextMuted, fontSize = 11.sp)
                        }
                        Text("EN", fontWeight = FontWeight.Bold, color = BrightCyan, fontSize = 14.sp)
                    }
                }
            }

            // About SmartLecture AI
            SettingsSectionHeader(title = "ABOUT SMARTLECTURE AI")
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
                    Text(
                        text = "SmartLecture AI v2.4.0",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Learn smarter. Remember more.\nDesigned with Liquid Glass & Gemini Architecture for academic excellence.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = CyanAccent,
        letterSpacing = 1.sp
    )
}
