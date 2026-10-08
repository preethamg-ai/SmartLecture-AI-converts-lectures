package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class BottomNavDestination {
    HOME, LIBRARY, AI, ANALYTICS, PROFILE
}

@Composable
fun FloatingBottomNavBar(
    currentDestination: BottomNavDestination,
    onNavigate: (BottomNavDestination) -> Unit,
    onAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        val navShape = RoundedCornerShape(32.dp)

        // Glass Navigation pill container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = navShape,
                    spotColor = CyanAccent.copy(alpha = 0.35f),
                    ambientColor = Color.Black.copy(alpha = 0.6f)
                )
                .clip(navShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x35121B33),
                            Color(0x450A1024),
                            Color(0x50060913)
                        )
                    )
                )
                .border(
                    border = BorderStroke(
                        1.2.dp,
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.08f),
                                CyanAccent.copy(alpha = 0.3f)
                            )
                        )
                    ),
                    shape = navShape
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavBarItem(
                    label = "Home",
                    iconSelected = Icons.Filled.Home,
                    iconUnselected = Icons.Outlined.Home,
                    isSelected = currentDestination == BottomNavDestination.HOME,
                    onClick = { onNavigate(BottomNavDestination.HOME) },
                    testTag = "nav_home"
                )

                // Library
                NavBarItem(
                    label = "Library",
                    iconSelected = Icons.Filled.LibraryBooks,
                    iconUnselected = Icons.Outlined.LibraryBooks,
                    isSelected = currentDestination == BottomNavDestination.LIBRARY,
                    onClick = { onNavigate(BottomNavDestination.LIBRARY) },
                    testTag = "nav_library"
                )

                // Spacer for Center elevated AI Button
                Spacer(modifier = Modifier.width(52.dp))

                // Analytics
                NavBarItem(
                    label = "Analytics",
                    iconSelected = Icons.Filled.BarChart,
                    iconUnselected = Icons.Outlined.BarChart,
                    isSelected = currentDestination == BottomNavDestination.ANALYTICS,
                    onClick = { onNavigate(BottomNavDestination.ANALYTICS) },
                    testTag = "nav_analytics"
                )

                // Profile
                NavBarItem(
                    label = "Profile",
                    iconSelected = Icons.Filled.Person,
                    iconUnselected = Icons.Outlined.Person,
                    isSelected = currentDestination == BottomNavDestination.PROFILE,
                    onClick = { onNavigate(BottomNavDestination.PROFILE) },
                    testTag = "nav_profile"
                )
            }
        }

        // Center Elevated Glowing AI Button
        CenterAiFab(
            onClick = onAiClick,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun NavBarItem(
    label: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) CyanAccent else TextMuted,
        animationSpec = tween(200),
        label = "NavIconColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = tween(200),
        label = "NavIconScale"
    )

    Column(
        modifier = Modifier
            .testTag(testTag)
            .height(52.dp)
            .width(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(bounded = false, color = CyanAccent),
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) iconSelected else iconUnselected,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else TextMuted
        )
    }
}

@Composable
private fun CenterAiFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .testTag("nav_ai_center_fab")
            .offset(y = (-10).dp)
            .size(58.dp)
            .shadow(
                elevation = 14.dp,
                shape = CircleShape,
                spotColor = CyanAccent.copy(alpha = 0.85f),
                ambientColor = NeonViolet.copy(alpha = 0.5f)
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(
                        BrightCyan,
                        ElectricBlue,
                        NeonViolet
                    )
                )
            )
            .border(
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.7f)),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(color = Color.White),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = "Ask SmartLecture AI",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}
