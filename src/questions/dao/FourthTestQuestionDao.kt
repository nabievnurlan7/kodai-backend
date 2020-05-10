package com.futuris.questions.dao

import org.jetbrains.exposed.sql.Table

object FourthTestQuestionDao : Table("t4_korrup") {
    val id = integer("id").primaryKey()
    val var1Ru = varchar("var1_ru", length = 512)
    val var2Ru = varchar("var2_ru", length = 512)

    val var1Kz = varchar("var1_kz", length = 512)
    val var2Kz = varchar("var2_kz", length = 512)
}