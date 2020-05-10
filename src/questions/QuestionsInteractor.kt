package com.futuris.questions

import com.futuris.RU_LANG
import com.futuris.questions.dao.FirstTestQuestionDao
import com.futuris.questions.dao.FourthTestQuestionDao
import com.futuris.questions.dao.SecondTestQuestionDao
import com.futuris.questions.dao.ThirdTestQuestionDao
import com.google.gson.Gson
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class QuestionsInteractor {

    fun getFirstTestQuestions(lang: String): String {
        var json = ""
        transaction {
            val result = FirstTestQuestionDao.selectAll().orderBy(FirstTestQuestionDao.id, true)
            val arrayList = ArrayList<QuestionTest1>()
            result.forEach { arrayList.add(getQuestion1(lang, it)) }
            json = Gson().toJson(arrayList)
        }
        return json
    }

    private fun getQuestion1(lang: String, question: ResultRow): QuestionTest1 =
        if (lang == RU_LANG) {
            QuestionTest1(
                id = question[FirstTestQuestionDao.id],
                question = question[FirstTestQuestionDao.questionRu]
            )
        } else {
            QuestionTest1(
                id = question[FirstTestQuestionDao.id],
                question = question[FirstTestQuestionDao.questionKz]
            )
        }

    fun getSecondTestQuestions(lang: String): String {
        var json = ""
        transaction {
            val result = SecondTestQuestionDao.selectAll().orderBy(SecondTestQuestionDao.id, true)
            val arrayList = ArrayList<QuestionTest2>()
            result.forEach { arrayList.add(getQuestion2(lang, it)) }
            json = Gson().toJson(arrayList)
        }
        return json
    }

    private fun getQuestion2(lang: String, question: ResultRow): QuestionTest2 =
        if (lang == RU_LANG) {
            QuestionTest2(
                id = question[SecondTestQuestionDao.id],
                question = question[SecondTestQuestionDao.questionRu],
                var1 = question[SecondTestQuestionDao.var1Ru],
                var2 = question[SecondTestQuestionDao.var2Ru],
                var3 = question[SecondTestQuestionDao.var3Ru]
            )
        } else {
            QuestionTest2(
                id = question[SecondTestQuestionDao.id],
                question = question[SecondTestQuestionDao.questionKz],
                var1 = question[SecondTestQuestionDao.var1Kz],
                var2 = question[SecondTestQuestionDao.var2Kz],
                var3 = question[SecondTestQuestionDao.var3Kz]
            )
        }

    fun getThirdTestQuestions(lang: String): String {
        var json = ""
        transaction {
            val result = ThirdTestQuestionDao.selectAll().orderBy(ThirdTestQuestionDao.id, true)
            val arrayList = ArrayList<QuestionTest3>()
            result.forEach { arrayList.add(getQuestion3(lang, it)) }
            json = Gson().toJson(arrayList)
        }
        return json
    }

    private fun getQuestion3(lang: String, question: ResultRow): QuestionTest3 =
        if (lang == RU_LANG) {
            QuestionTest3(
                id = question[ThirdTestQuestionDao.id],
                question = question[ThirdTestQuestionDao.questionRu]
            )
        } else {
            QuestionTest3(
                id = question[ThirdTestQuestionDao.id],
                question = question[ThirdTestQuestionDao.questionKz]
            )
        }

    fun getFourthTestQuestions(lang: String): String {
        var json = ""
        transaction {
            val result = FourthTestQuestionDao.selectAll().orderBy(FourthTestQuestionDao.id, true)
            val arrayList = ArrayList<QuestionTest4>()
            result.forEach { arrayList.add(getQuestion4(lang, it)) }
            json = Gson().toJson(arrayList)
        }
        return json
    }

    private fun getQuestion4(lang: String, question: ResultRow): QuestionTest4 =
        if (lang == RU_LANG) {
            QuestionTest4(
                id = question[FourthTestQuestionDao.id],
                var1 = question[FourthTestQuestionDao.var1Ru],
                var2 = question[FourthTestQuestionDao.var2Ru]
            )
        } else {
            QuestionTest4(
                id = question[FourthTestQuestionDao.id],
                var1 = question[FourthTestQuestionDao.var1Kz],
                var2 = question[FourthTestQuestionDao.var2Kz]
            )
        }
}