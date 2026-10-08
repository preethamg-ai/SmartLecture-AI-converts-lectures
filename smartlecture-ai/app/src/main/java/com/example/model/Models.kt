package com.example.model

data class Lecture(
    val id: String,
    val title: String,
    val subject: String,
    val duration: String,
    val date: String,
    val fileType: String,
    val fileSize: String,
    val notesGenerated: Boolean = true,
    val quizGenerated: Boolean = true,
    val summary: String,
    val conceptsCount: Int = 18,
    val topicsCount: Int = 7,
    val progressPercent: Float = 1.0f,
    val isFavorite: Boolean = false
)

data class DefinitionItem(
    val term: String,
    val definition: String
)

data class FaqItem(
    val question: String,
    val answer: String
)

data class NoteDocument(
    val id: String,
    val lectureId: String,
    val title: String,
    val subject: String,
    val lastEdited: String,
    val summary: String,
    val overview: String,
    val keyConcepts: List<String>,
    val importantPoints: List<String>,
    val definitions: List<DefinitionItem>,
    val examples: List<String>,
    val examTopics: List<String>,
    val faqs: List<FaqItem>,
    val pageCount: Int = 8,
    val isFavorite: Boolean = false
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val topic: String
)

data class Quiz(
    val id: String,
    val lectureId: String,
    val title: String,
    val subject: String,
    val questions: List<QuizQuestion>
)

data class QuizResult(
    val quizId: String,
    val scorePercent: Int,
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val timeTaken: String,
    val accuracy: Int,
    val understoodTopics: List<String>,
    val topicsToRevise: List<String>,
    val aiRecommendation: String
)

data class WeeklyScorePoint(
    val day: String,
    val score: Float // 0.0f - 1.0f
)

data class SubjectProficiency(
    val subject: String,
    val scorePercent: Int,
    val lectureCount: Int
)

data class AnalyticsData(
    val totalStudyTime: String,
    val quizAccuracy: Int,
    val lecturesCompleted: Int,
    val learningStreak: Int,
    val weeklyScores: List<WeeklyScorePoint>,
    val subjectProficiency: List<SubjectProficiency>,
    val aiAnalysis: String,
    val recommendedStudyTime: String = "30 minutes"
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val description: String,
    val isUnlocked: Boolean = true
)

data class UserProfile(
    val name: String,
    val email: String,
    val institution: String,
    val streakDays: Int,
    val completedLectures: Int,
    val quizAverage: Int,
    val badges: List<AchievementBadge>
)

data class SearchResultItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: SearchCategory,
    val targetId: String
)

enum class SearchCategory {
    LECTURE, NOTE, QUESTION, TOPIC
}
