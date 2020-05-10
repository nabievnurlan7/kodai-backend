package com.futuris.di


import com.futuris.DummyDataInteractor
import org.koin.dsl.module

val mainModule = module(createdAtStart = true) {

    single { DummyDataInteractor() }
}