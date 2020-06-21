package com.futuris

import org.jetbrains.exposed.sql.Table

object TagDao : Table("tags") {
    val id = TagDao.integer("id").primaryKey()
    val tagName = TagDao.text("name")
    val questionId = TagDao.integer("question_id")
}