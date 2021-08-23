package me.jimmyshaw.simplequiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_android, true),
        Question(R.string.question_animal, false),
        Question(R.string.question_earth, false),
        Question(R.string.question_tallest_building, true),
        Question(R.string.question_us, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}