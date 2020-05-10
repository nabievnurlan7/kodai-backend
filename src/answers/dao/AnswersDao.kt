package com.futuris.questions

import org.jetbrains.exposed.sql.Table

object AnswersDao : Table("answers") {
    val id = integer("id").primaryKey()

    val code_id = integer("code_id")

    val firstname = varchar("firstname",40)
    val lastname = varchar("lastname", 40)

    var test1_result1 = integer("test1_result1")
    val test1_result2 = integer("test1_result2")

    val test2_max = integer("test2_maxresult")
    val test2_min = integer("test2_minresult")

    val test3_result = integer("test3_result")

    val test4_result = integer("test4_result")
}