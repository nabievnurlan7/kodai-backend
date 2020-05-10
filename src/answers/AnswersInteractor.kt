package com.futuris.answers

import com.futuris.codes.CodeInteractor
import com.futuris.processor.Processor
import com.futuris.questions.AnswersDao
import com.futuris.results.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class AnswersInteractor {
    private val answersProcessor = Processor()
    private val codeInteractor = CodeInteractor()
    private val gson = GsonBuilder().setLenient().create()

    fun processAnswers(json: String): String {
        val totalResult = TotalResult(0, null, null, null, null, null)


        val jsonCode = getDataFromJson(json, "code")
        val code: String = gson.fromJson(jsonCode, object : TypeToken<String>() {}.type)
        totalResult.codeId = codeInteractor.getCodeId(code)

        val jsonName = getDataFromJson(json, "name")
        val name: Username = gson.fromJson(jsonName, object : TypeToken<Username>() {}.type)
        totalResult.name = name

        totalResult.firstTestResult = getResults(json, "test1") as FirstTestResult
        totalResult.secondTestResult = getResults(json, "test2") as SecondTestResult
        totalResult.thirdTestResult = getResults(json, "test3") as ThirdTestResult
        totalResult.fourthTestResult = getResults(json, "test4") as FourthTestResult

        return saveResults(totalResult)
    }

    private fun getResults(json: String, testType: String): TestResult {
        val answerJson = getDataFromJson(json, testType)

         fun <T> processToList(jsonToProcess: String): List<T> =
            gson.fromJson(jsonToProcess, object : TypeToken<List<T>>() {}.type)

        val answers = when (testType) {
            "test1" -> processToList<FirstTestAnswers>(answerJson)
            "test2" -> processToList<SecondTestAnswers>(answerJson)
            "test3" -> processToList<ThirdTestAnswers>(answerJson)
            "test4" -> processToList<FourthTestAnswers>(answerJson)
            else -> emptyList()
        }

        return answersProcessor.processAnswers(answers, testType)
    }

    private fun getDataFromJson(json: String, type: String): String {
        val jsonTree = JsonParser().parse(json)
        val jsonObject = jsonTree.asJsonObject
        return jsonObject.get(type).toString()
    }

    private fun saveResults(totalResult: TotalResult): String {
        return try {
            transaction {
                AnswersDao.insert {
                    it[code_id] = totalResult.codeId

                    it[firstname] = totalResult.name?.firstname
                    it[lastname] = totalResult.name?.lastname

                    it[test1_result1] = totalResult.firstTestResult?.result1
                    it[test1_result2] = totalResult.firstTestResult?.result2

                    it[test2_max] = totalResult.secondTestResult?.maxResult
                    it[test2_min] = totalResult.secondTestResult?.minResult

                    it[test3_result] = totalResult.thirdTestResult?.result

                    it[test4_result] = totalResult.fourthTestResult?.result
                }
            }
            "ok"
        } catch (e: Exception) {
            "Error ${e.toString()}"
        }
    }
}