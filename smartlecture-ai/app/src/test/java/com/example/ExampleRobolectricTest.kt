package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.service.SmartLectureService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SmartLecture AI", appName)
  }

  @Test
  fun `smart lecture service generates notes and quizzes correctly`() = runBlocking {
    val service = SmartLectureService.instance
    val uploaded = service.uploadLecture("Algorithms_Lecture.pdf", "PDF", "3.2 MB", "Algorithms")
    assertNotNull(uploaded)

    val noteDoc = service.generateNotes(uploaded.id)
    assertNotNull(noteDoc)
    assertTrue(noteDoc.keyConcepts.isNotEmpty())

    val quiz = service.generateQuiz(uploaded.id)
    assertNotNull(quiz)
    assertTrue(quiz.questions.isNotEmpty())

    val result = service.submitQuiz(quiz.id, mapOf(0 to 0, 1 to 2))
    assertNotNull(result)
  }
}
