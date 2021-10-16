package com.futuris.question

import org.jetbrains.exposed.sql.Table

object QuestionDao : Table("questions") {
    val id = integer("id").primaryKey()
    val question = text("text")
    val type = integer("type")
    val var1 = text("var1")
    val var2 = text("var2")
    val var3 = text("var3")
    val var4 = text("var4")
    val correct = integer("correct")
}