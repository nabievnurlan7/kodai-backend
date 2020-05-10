package com.futuris.codes

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class CodeInteractor {

    fun checkCode(typedCode: String): String {
        var count: Int = 0
        transaction {
            val result = CodeDao.select { CodeDao.code.eq(typedCode) }
            count = result.count()
        }

        return if (count >= 1) {
            "ok"
        } else {
            "error"
        }
    }

    fun getCodeId(code: String): Int {
        var codeId: Int = 0
        transaction {
            CodeDao.slice(CodeDao.id)
                .select { CodeDao.code.eq(code) }
                .forEach { codeId = it[CodeDao.id] }
        }

        return codeId
    }
}