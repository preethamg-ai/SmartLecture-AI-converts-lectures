package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.model.AchievementBadge
import com.example.service.SmartLectureService
import com.example.ui.components.AiPulsingOrb
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    onNavigateSettings: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val service = SmartLectureService.instance
    val profile by service.userProfile.collectAsState()

    LiquidGlassBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .testTag("screen_profile"),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Top Bar with Settings gear
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scholar Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = onNavigateSettings,
                        modifier = Modifier
                            .testTag("profile_settings_button")
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x18FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }

            // User Identity Glass Hero Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = Color(0x20152445),
                    borderColor = CyanAccent.copy(alpha = 0.45f),
                    isActive = true
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CyanAccent, ElectricBlueLight, NeonViolet)
                                    )
                                )
                                .border(2.5.dp, Color.White.copy(alpha = 0.7f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AM",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = profile.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = profile.email,
                            fontSize = 13.sp,
                            color = CyanAccent
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = profile.institution,
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ProfileStatPill(
                                label = "Streak",
                                value = "🔥 ${profile.streakDays}d",
                                color = WarningAmber,
                                modifier = Modifier.weight(1f)
                            )
                            ProfileStatPill(
                                label = "Lectures",
                                value = "📚 ${profile.completedLectures}",
                                color = BrightCyan,
                                modifier = Modifier.weight(1f)
                            )
                            ProfileStatPill(
                                label = "Avg Quiz",
                                value = "🧠 ${profile.quizAverage}%",
                                color = NeonViolet,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Achievements & Badges Header
            item {
                Text(
                    text = "SCHOLAR ACHIEVEMENTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CyanAccent,
                    letterSpacing = 1.sp
                )
            }

            // Badges List
            items(profile.badges) { badge ->
                BadgeCard(badge = badge)
            }

            // Action / Logout
            item {
                GlassSecondaryButton(
                    text = "Sign Out",
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "profile_logout_button",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = NeonPink,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileStatPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x18FFFFFF))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun BadgeCard(badge: AchievementBadge) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x16FFFFFF)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x207C3AED)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = badge.iconEmoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = badge.description,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SuccessGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "UNLOCKED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }
        }
    }
}
