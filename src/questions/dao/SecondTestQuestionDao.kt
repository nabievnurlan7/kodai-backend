package com.futuris.questions.dao

import org.jetbrains.exposed.sql.Table

object SecondTestQuestionDao : Table("t2_ved_napr") {
    val id = integer("id").primaryKey()
    val questionRu = varchar("question_ru", length = 512)
    val var1Ru = varchar("var1_ru", length = 512)
    val var2Ru = varchar("var2_ru", length = 512)
    val var3Ru = varchar("var3_ru", length = 512)

    val questionKz = varchar("question_kz", length = 512)
    val var1Kz = varchar("var1_kz", length = 512)
    val var2Kz = varchar("var2_kz", length = 512)
    val var3Kz = varchar("var3_kz", length = 512)
}