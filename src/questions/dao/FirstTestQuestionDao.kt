package com.futuris.questions.dao

import org.jetbrains.exposed.sql.Table

object FirstTestQuestionDao : Table("t1_samod_kand") {
    val id = integer("id").primaryKey()
    val questionRu = varchar("question_ru", length = 512)
    val questionKz = varchar("question_kz", length = 512)
}