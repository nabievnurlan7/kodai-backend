package com.futuris.codes

import org.jetbrains.exposed.sql.Table

object CodeDao : Table("code_test") {
    val id = integer("id").primaryKey()
    val code = varchar("code", length = 250)
    val usageQuantity = integer("usage_quantity")
    val showResult = bool("show_result")
    val description = text("description")
}