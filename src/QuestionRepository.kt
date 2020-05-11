package com.futuris

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class QuestionRepository {

    private var interviewQuestions: MutableList<QuestionInterview> = mutableListOf()

    init {
        loadCacheQuestions()
    }

    private fun loadCacheQuestions() {
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

    fun getInterviewQuestions(quantity: Int): List<QuestionInterview> = interviewQuestions.shuffled().take(quantity)

    data class QuestionInterview(
        val id: Int,
        val question: String
    )

    companion object {
        private const val QUESTION_TYPE_INTERVIEW = 1
        private const val QUESTION_TYPE_QUIZ = 2
    }
}