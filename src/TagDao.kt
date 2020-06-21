package com.futuris

import org.jetbrains.exposed.sql.Table

object TagDao : Table("tags") {
    val id = TagDao.integer("id").primaryKey()
    val tagName = TagDao.text("name")
}