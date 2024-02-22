package edu.uw.ischool.qnho.quizdroid

import android.util.Log

// ============================================================
// Application object
class QuizApp : android.app.Application() {
    lateinit var quizzes : TopicRepository.ActualTopicRepository
    override fun onCreate() {
        super.onCreate()

        quizzes = TopicRepository.ActualTopicRepository(this)

        Log.i("APPLICATION", "RUNNING")
    }
}

// ============================================================
// Domain objects
data class Topic (val title: String, val desc: String, val questions: List<Question>, val icon: Int) {}

data class Question (val text: String, val ans: List<String>, val correct: Int) {}