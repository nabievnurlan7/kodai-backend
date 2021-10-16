package com.futuris.common

import org.jetbrains.exposed.sql.Table

object CrossDao : Table("crossing") {
    val tagId = CrossDao.integer("tag_id").primaryKey()
    val questionId = CrossDao.integer("question_id")
}