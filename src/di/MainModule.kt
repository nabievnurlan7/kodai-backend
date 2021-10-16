package com.futuris.di


import com.futuris.job.JobRepository
import com.futuris.question.QuestionRepository
import org.koin.dsl.module

val mainModule = module(createdAtStart = true) {

    single { QuestionRepository() }
    single { JobRepository() }
}