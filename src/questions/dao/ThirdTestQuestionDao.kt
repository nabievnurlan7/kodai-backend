package com.futuris.questions.dao

import org.jetbrains.exposed.sql.Table

object ThirdTestQuestionDao : Table("t3_samoreg") {
    val id = integer("id").primaryKey()
    val questionRu = varchar("question_ru", length = 512)
    val questionKz = varchar("question_kz", length = 512)
}