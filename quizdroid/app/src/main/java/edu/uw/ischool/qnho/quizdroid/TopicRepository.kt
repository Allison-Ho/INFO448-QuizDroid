package edu.uw.ischool.qnho.quizdroid

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.JsonReader
import android.util.Log
import android.widget.Toast
import java.io.IOException

// ============================================================
// Repository
interface TopicRepository {
    fun getQuiz() : List<Topic>
    fun quizByTopic(topic: String) : List<Question>?
    fun getTopic() : List<Pair<String, String>>
    fun getTopicInfo(topic: String) : Array<Any>

    class ActualTopicRepository(val application: Application) : TopicRepository {
        private val quizzes: MutableList<Topic> = loadFile()

        override fun getQuiz(): List<Topic> {
            return quizzes
        }

        override fun quizByTopic(topic: String): List<Question>? {
            return quizzes.find { it.title == topic }?.questions
        }

        override fun getTopic(): List<Pair<String, String>> {
            return quizzes.map { Pair(it.title, it.desc) }
        }

        override fun getTopicInfo(topic: String): Array<Any> {
            val foundTopic = quizzes.find { it.title == topic }
            return if (foundTopic != null) {
                arrayOf(foundTopic.desc, foundTopic.questions.size, foundTopic.icon)
            } else {
                arrayOf("", -1, -1)
            }
        }

        fun loadFile(): MutableList<Topic> {
            val content: MutableList<Topic> = mutableListOf()
            //structure:
            //quizzes = array of maps<String, Any>
            //questions = array of maps<Pair(String, String), ArrayList<String>>
            Log.i("TEST", "in repo: ${application.filesDir.toString()}")
            try{
                application.openFileInput("questions.json").apply {
                    val jsonreader = JsonReader(reader())
                    jsonreader.beginArray()
                    while(jsonreader.hasNext()) {
                        content.add(getQuiz(jsonreader))
                    }
                    jsonreader.endArray()
                    close()
                }
                Log.i("PLEASE", "why $content")
            }catch (ioEx : IOException) {
                Log.e("DEBUG", "Error opening file", ioEx)
            }

            return content
        }

        private fun getQuiz(reader: JsonReader): Topic {
            var quizContent: Topic = Topic("", "", arrayListOf(), 0)
            val allQuestions: ArrayList<Question> = arrayListOf()

            reader.beginObject()
            while (reader.hasNext()) {
                assert(reader.nextName() == "title")
                val title = reader.nextString()
                Log.i("CRYING", "why $title")
                assert(reader.nextName() == "desc")
                val desc = reader.nextString()
                assert(reader.nextName() == "questions")
                reader.beginArray()
                while (reader.hasNext()) {
                    allQuestions.add(getQuestions(reader))
                }
                reader.endArray()

                quizContent = Topic(title, desc, allQuestions, R.mipmap.ic_launcher_round)
            }

            reader.endObject()
            return quizContent
        }

        private fun getQuestions(reader: JsonReader): Question {
            var question : Question = Question("", arrayListOf(), 0)
            val options : ArrayList<String> = arrayListOf()

            reader.beginObject()
            while(reader.hasNext()) {
                assert(reader.nextName() == "text")
                val text = reader.nextString()
                assert(reader.nextName() == "answer")
                val ans = reader.nextString()
                assert(reader.nextName() == "answers")
                reader.beginArray()
                while(reader.hasNext()) {
                    options.add(reader.nextString())
                }
                reader.endArray()
                question = Question(text, options, ans.toInt())
            }

            reader.endObject()
            return question
        }
    }
}
