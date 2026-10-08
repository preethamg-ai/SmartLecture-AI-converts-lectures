package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.model.QuizResult
import com.example.ui.components.AskAiBottomSheet
import com.example.ui.components.BottomNavDestination
import com.example.ui.components.FloatingBottomNavBar
import com.example.ui.screens.*

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object Auth : Screen()
    object Home : Screen()
    object Upload : Screen()
    data class Processing(val lectureId: String, val generateQuiz: Boolean) : Screen()
    data class Notes(val lectureId: String) : Screen()
    data class LectureDetails(val lectureId: String) : Screen()
    data class QuizScreenDest(val lectureId: String) : Screen()
    data class QuizResultDest(val result: QuizResult) : Screen()
    object Analytics : Screen()
    object Library : Screen()
    object Search : Screen()
    object Profile : Screen()
    object Settings : Screen()
}

@Composable
fun SmartLectureApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var previousScreen by remember { mutableStateOf<Screen?>(null) }
    var showAiSheet by remember { mutableStateOf(false) }

    fun navigateTo(screen: Screen) {
        previousScreen = currentScreen
        currentScreen = screen
    }

    // Determine bottom nav destination if current screen matches
    val currentBottomNavDest = when (currentScreen) {
        Screen.Home -> BottomNavDestination.HOME
        Screen.Library -> BottomNavDestination.LIBRARY
        Screen.Analytics -> BottomNavDestination.ANALYTICS
        Screen.Profile -> BottomNavDestination.PROFILE
        else -> null
    }

    // Back button handling
    BackHandler(enabled = currentScreen !is Screen.Splash && currentScreen !is Screen.Home) {
        when (currentScreen) {
            Screen.Settings -> currentScreen = Screen.Profile
            Screen.Library, Screen.Analytics, Screen.Profile -> currentScreen = Screen.Home
            is Screen.QuizResultDest -> currentScreen = Screen.Home
            is Screen.QuizScreenDest -> currentScreen = previousScreen ?: Screen.Home
            is Screen.Notes -> currentScreen = previousScreen ?: Screen.Home
            is Screen.LectureDetails -> currentScreen = Screen.Home
            Screen.Upload, Screen.Search -> currentScreen = Screen.Home
            is Screen.Processing -> currentScreen = Screen.Upload
            Screen.Auth -> currentScreen = Screen.Onboarding
            Screen.Onboarding -> currentScreen = Screen.Splash
            else -> currentScreen = Screen.Home
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentBottomNavDest != null) {
                FloatingBottomNavBar(
                    currentDestination = currentBottomNavDest,
                    onNavigate = { dest ->
                        when (dest) {
                            BottomNavDestination.HOME -> navigateTo(Screen.Home)
                            BottomNavDestination.LIBRARY -> navigateTo(Screen.Library)
                            BottomNavDestination.ANALYTICS -> navigateTo(Screen.Analytics)
                            BottomNavDestination.PROFILE -> navigateTo(Screen.Profile)
                            BottomNavDestination.AI -> showAiSheet = true
                        }
                    },
                    onAiClick = { showAiSheet = true }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300))
                        .togetherWith(fadeOut(animationSpec = tween(200)))
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Splash -> {
                        SplashScreen(
                            onSplashFinished = { navigateTo(Screen.Onboarding) }
                        )
                    }

                    Screen.Onboarding -> {
                        OnboardingScreen(
                            onFinishOnboarding = { navigateTo(Screen.Auth) }
                        )
                    }

                    Screen.Auth -> {
                        AuthScreen(
                            onLoginSuccess = { navigateTo(Screen.Home) }
                        )
                    }

                    Screen.Home -> {
                        HomeScreen(
                            onNavigateUpload = { navigateTo(Screen.Upload) },
                            onNavigateNotes = { lecId -> navigateTo(Screen.Notes(lecId)) },
                            onNavigateQuiz = { lecId -> navigateTo(Screen.QuizScreenDest(lecId)) },
                            onNavigateLectureDetail = { lecId -> navigateTo(Screen.LectureDetails(lecId)) },
                            onNavigateLibrary = { navigateTo(Screen.Library) },
                            onNavigateProfile = { navigateTo(Screen.Profile) },
                            onNavigateSearch = { navigateTo(Screen.Search) },
                            onOpenAiSheet = { showAiSheet = true }
                        )
                    }

                    Screen.Upload -> {
                        UploadLectureScreen(
                            onBack = { navigateTo(Screen.Home) },
                            onStartProcessing = { lecId, genQuiz ->
                                navigateTo(Screen.Processing(lecId, genQuiz))
                            }
                        )
                    }

                    is Screen.Processing -> {
                        ProcessingScreen(
                            lectureId = screen.lectureId,
                            generateQuiz = screen.generateQuiz,
                            onProcessingFinished = { lecId, openQuiz ->
                                if (openQuiz) {
                                    navigateTo(Screen.QuizScreenDest(lecId))
                                } else {
                                    navigateTo(Screen.Notes(lecId))
                                }
                            }
                        )
                    }

                    is Screen.Notes -> {
                        AiNotesScreen(
                            lectureId = screen.lectureId,
                            onBack = { navigateTo(previousScreen ?: Screen.Home) },
                            onNavigateQuiz = { lecId -> navigateTo(Screen.QuizScreenDest(lecId)) }
                        )
                    }

                    is Screen.LectureDetails -> {
                        LectureDetailsScreen(
                            lectureId = screen.lectureId,
                            onBack = { navigateTo(Screen.Home) },
                            onNavigateNotes = { lecId -> navigateTo(Screen.Notes(lecId)) },
                            onStartQuiz = { lecId -> navigateTo(Screen.QuizScreenDest(lecId)) }
                        )
                    }

                    is Screen.QuizScreenDest -> {
                        QuizScreen(
                            lectureId = screen.lectureId,
                            onBack = { navigateTo(previousScreen ?: Screen.Home) },
                            onQuizSubmitted = { result -> navigateTo(Screen.QuizResultDest(result)) }
                        )
                    }

                    is Screen.QuizResultDest -> {
                        QuizResultScreen(
                            result = screen.result,
                            onReviewAnswers = { navigateTo(Screen.Notes("lec_1")) },
                            onTryAgain = { navigateTo(Screen.QuizScreenDest("lec_1")) },
                            onGoHome = { navigateTo(Screen.Home) }
                        )
                    }

                    Screen.Analytics -> {
                        AnalyticsScreen()
                    }

                    Screen.Library -> {
                        SavedNotesScreen(
                            onNavigateNoteDetail = { lecId -> navigateTo(Screen.Notes(lecId)) }
                        )
                    }

                    Screen.Search -> {
                        SearchScreen(
                            onBack = { navigateTo(Screen.Home) },
                            onNavigateLecture = { lecId -> navigateTo(Screen.LectureDetails(lecId)) },
                            onNavigateNotes = { lecId -> navigateTo(Screen.Notes(lecId)) },
                            onNavigateQuiz = { lecId -> navigateTo(Screen.QuizScreenDest(lecId)) }
                        )
                    }

                    Screen.Profile -> {
                        ProfileScreen(
                            onNavigateSettings = { navigateTo(Screen.Settings) },
                            onLogout = { navigateTo(Screen.Auth) }
                        )
                    }

                    Screen.Settings -> {
                        SettingsScreen(
                            onBack = { navigateTo(Screen.Profile) }
                        )
                    }
                }
            }

            // Global Ask AI Bottom Sheet
            if (showAiSheet) {
                AskAiBottomSheet(
                    onDismiss = { showAiSheet = false }
                )
            }
        }
    }
}
