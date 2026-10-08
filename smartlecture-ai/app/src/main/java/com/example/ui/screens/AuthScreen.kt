package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassPrimaryButton
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("alex.morgan@university.edu") }
    var password by remember { mutableStateOf("••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LiquidGlassBackground(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp)
                .testTag("screen_auth"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top Branding
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(CyanAccent, NeonViolet))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "SmartLecture AI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = if (isSignUp) "Create your AI learning account" else "Welcome back, scholar",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Premium Glass Login Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = Color(0x20FFFFFF),
                    borderColor = CyanAccent.copy(alpha = 0.35f),
                    elevation = 12.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        Text(
                            text = if (isSignUp) "Sign Up" else "Login",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Email Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = TextSecondary) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "Email",
                                    tint = CyanAccent
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x18FFFFFF),
                                unfocusedContainerColor = Color(0x0EFFFFFF),
                                focusedBorderColor = CyanAccent,
                                unfocusedBorderColor = GlassBorderLight,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Password Field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", color = TextSecondary) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Password",
                                    tint = CyanAccent
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = TextMuted
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_password_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x18FFFFFF),
                                unfocusedContainerColor = Color(0x0EFFFFFF),
                                focusedBorderColor = CyanAccent,
                                unfocusedBorderColor = GlassBorderLight,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Remember Me & Forgot Password
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { rememberMe = !rememberMe }
                            ) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CyanAccent,
                                        checkmarkColor = BgDeepDark,
                                        uncheckedColor = TextMuted
                                    )
                                )
                                Text("Remember", fontSize = 12.sp, color = TextSecondary)
                            }

                            TextButton(onClick = { errorMessage = "Password reset instructions sent to $email" }) {
                                Text("Forgot Password?", fontSize = 12.sp, color = CyanAccent)
                            }
                        }

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                fontSize = 12.sp,
                                color = SuccessGreen,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Continue Button
                        GlassPrimaryButton(
                            text = if (isSignUp) "Create Account" else "Continue",
                            onClick = onLoginSuccess,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "auth_continue_button"
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Continue with Google Button
                        GlassSecondaryButton(
                            text = "Continue with Google",
                            onClick = onLoginSuccess,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "auth_google_button",
                            icon = {
                                Text("G", fontWeight = FontWeight.Bold, color = BrightCyan, fontSize = 16.sp)
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Switch to Sign Up / Login
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isSignUp) "Already have an account?" else "Don't have an account?",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSignUp) "Login" else "Create Account",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CyanAccent,
                                modifier = Modifier
                                    .clickable {
                                        isSignUp = !isSignUp
                                        errorMessage = null
                                    }
                                    .testTag("auth_toggle_mode")
                            )
                        }
                    }
                }
            }
        }
    }
}
