package edu.uw.ischool.qnho.quizdroid

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TopicRepositoryTest {
    val repo = TopicRepository.ActualTopicRepository()
    @Test
    fun test_get_quiz() {
        val allQuizzes = repo.getQuiz()
        assertEquals(3, allQuizzes.size)
    }

    fun test_get_by_topic() {
        val byTopic = repo.quizByTopic("math")
        assertEquals("1 + 1", byTopic?.get(0)?.text)
        assertEquals(listOf("2", "4", "6", "8"), byTopic?.get(0)?.ans)
        assertEquals(3, byTopic?.size ?: 0)
    }

    fun test_get_topic() {
        val topics = repo.getTopic()
        assertEquals(3, topics.size)
        assertEquals("math", topics[0].first)
        assertEquals("Do some math", topics[0].second)
    }

    fun test_get_topic_info() {
        val info = repo.getTopicInfo("marvel")
        assertEquals(3, info.size)
        assertEquals("Fun facts about physics", info[0])
        assertEquals(3, info[1])
        assertEquals(R.mipmap.ic_launcher_round, info[2])
    }
}