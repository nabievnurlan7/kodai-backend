package com.futuris.di


import com.futuris.QuestionRepository
import org.koin.dsl.module

val mainModule = module(createdAtStart = true) {

    single { QuestionRepository() }
}