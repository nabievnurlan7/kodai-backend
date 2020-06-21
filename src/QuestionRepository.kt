package com.futuris

import com.futuris.di.CrossDao
import com.google.gson.Gson
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class QuestionRepository {

    private var interviewQuestions: MutableList<QuestionInterview> = mutableListOf()

    init {
        loadAndCacheQuestions()
    }

    fun getInterviewQuestions(quantity: Int): String {
        val list = interviewQuestions.shuffled().take(quantity)
        return Gson().toJson(list)
    }

    fun getInterviewQuestionsByTag(tagId: Int, quantity: Int): String {
        val questionIdList = getQuestionsId(tagId)
        val list = getQuestionsByQueriedIds(questionIdList).shuffled().take(quantity)
        return Gson().toJson(list)
    }

    private fun getQuestionsId(tagId: Int): List<Int> {
        val arrayList = ArrayList<Int>()
        transaction {
            val result = CrossDao.select { CrossDao.tagId.eq(tagId) }
            result.forEach { arrayList.add(it[CrossDao.questionId]) }
        }

        return arrayList
    }

    private fun getQuestionsByQueriedIds(questionIdList: List<Int>): ArrayList<QuestionInterview> {
        val arrayList = ArrayList<QuestionInterview>()
        questionIdList.forEach {
            val result = QuestionDao.select { QuestionDao.id.eq(it) }
            result.firstOrNull {
                arrayList.add(
                        QuestionInterview(
                                id = it[QuestionDao.id],
                                question = it[QuestionDao.question]
                        )
                )
            }
        }

        return arrayList
    }

    private fun loadAndCacheQuestions() {
        transaction {
            val result =
                    QuestionDao.select { QuestionDao.type.eq(QUESTION_TYPE_INTERVIEW) }.orderBy(QuestionDao.id, true)
            val arrayList = ArrayList<QuestionInterview>()
            result.forEach {
                arrayList.add(
                        QuestionInterview(
                                id = it[QuestionDao.id],
                                question = it[QuestionDao.question]
                        )
                )
            }

            interviewQuestions = arrayList
        }
    }

    fun getTags(): String {
        val arrayList = ArrayList<Tag>()
        transaction {
            val result = TagDao.selectAll().orderBy(TagDao.id, true)
            result.forEach {
                arrayList.add(Tag(it[TagDao.id], it[TagDao.tagName]))
            }
        }
        return Gson().toJson(arrayList)
    }

    data class QuestionInterview(
            val id: Int,
            val question: String
    )

    data class Tag(
            val id: Int,
            val tagName: String
    )

    companion object {
        private const val QUESTION_TYPE_INTERVIEW = 1
        private const val QUESTION_TYPE_QUIZ = 2
    }
}