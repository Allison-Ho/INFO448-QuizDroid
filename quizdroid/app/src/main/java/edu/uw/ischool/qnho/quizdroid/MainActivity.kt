package edu.uw.ischool.qnho.quizdroid

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.LinearLayout
import java.io.File
import java.io.IOException

// ============================================================
// UI layer
class MainActivity : AppCompatActivity() {
    private val bgThread = Threading(this)
    private val fragmentManager = supportFragmentManager
    private val homePage = HomePage()

    var allQuizzes: List<Topic> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bgThread.start()

        val quizApp = (application as QuizApp)
        val repo = quizApp.quizzes
        allQuizzes = repo.getQuiz()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.app, homePage).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgThread.stopThread()
    }

    private fun saveToPreferences(url: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("quizdroid", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("URL", url)
        editor.apply()
    }
}