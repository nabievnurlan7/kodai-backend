package com.futuris.di

import org.jetbrains.exposed.sql.Table

object CrossDao : Table("cross") {
    val tagId = CrossDao.integer("tag_id").primaryKey()
    val questionId = CrossDao.integer("question_id")
}