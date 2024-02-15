package edu.uw.ischool.qnho.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout

// ============================================================
// Application object
class QuizApp : android.app.Application() {
    lateinit var quizzes : TopicRepository.ActualTopicRepository
    override fun onCreate() {
        super.onCreate()

        quizzes = TopicRepository.ActualTopicRepository()

        Log.i("APPLICATION", "RUNNING")
    }
}

// ============================================================
// Repository
interface TopicRepository {
    fun getQuiz() : List<Topic>
    fun quizByTopic(topic: String) : List<Question>?
    fun getTopic() : List<Pair<String, String>>
    fun getTopicInfo(topic: String) : Pair<String, Int>

    class ActualTopicRepository : TopicRepository {
        val  math = listOf<Question>(Question("1 + 1", listOf("2", "4", "6", "8"), 0),
                                     Question("4 + 6", listOf("20", "14", "10", "18"), 2),
                                     Question("3 * 5", listOf("5", "15", "25", "35"), 1))

        val  physics = listOf<Question>(Question("Electric power is typically measured in what units", listOf("Blah", "Watts", "Yay", "Ohms"), 1),
            Question("What is the earth's primary source of energy", listOf("Fire", "Water", "Wind", "The sun"), 3),
            Question("Who became famous after being hit in the head by an apple?", listOf("Einstein", "Galileo", "Newton", "Copernicus"), 2))

        val  marvel = listOf<Question>(Question("Where do Thor and Loki live?", listOf("Midgard", "Asgard", "Nidavellir", "Jotunheim"), 0),
            Question("Who is the first super hero of the MCU?", listOf("Captain America", "Thor", "Iron Man", "Doctor Strange"), 2),
            Question("What is the name of the infinity stone that Thanos took from Loki?", listOf("Time stone", "Soul stone", "Mind stone", "Space stone"), 2))

        val quizzes : MutableList<Topic> = mutableListOf(
            Topic("Math", "Do some math", "Quiz about math", math),
            Topic("Physics", "i love physics", "Fun facts about physics", physics),
            Topic("Marvel", "i love marvel", "Fun facts about the MCU and marvel universe", marvel)
        )

        override fun getQuiz(): List<Topic> {
            return quizzes
        }

        override fun quizByTopic(topic: String): List<Question>? {
            return quizzes.find { it.title == topic }?.questions
        }

        override fun getTopic(): List<Pair<String, String>> {
            return quizzes.map { Pair(it.title, it.shortDesc) }
        }

        override fun getTopicInfo(topic: String): Pair<String, Int> {
            val foundTopic = quizzes.find { it.title == topic }
            return if (foundTopic != null) {
                Pair(foundTopic.longDesc, foundTopic.questions.size)
            } else {
                Pair("", -1)
            }
        }
    }
}

// ============================================================
// Domain objects
data class Topic (val title: String, val shortDesc: String, val longDesc: String, val questions: List<Question>) {}

data class Question (val text: String, val ans: List<String>, val correct: Int) {}



// ============================================================
// UI layer
class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val homePage = HomePage()

    var allQuizzes: List<Topic> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizApp = (application as QuizApp)
        val repo = quizApp.quizzes
        allQuizzes = repo.getQuiz()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.app, homePage).commit()
    }
}