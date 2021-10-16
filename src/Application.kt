package com.futuris

import com.fasterxml.jackson.databind.SerializationFeature
import com.futuris.di.mainModule
import com.futuris.job.JobRepository
import com.futuris.login.LoginInteractor
import com.futuris.question.QuestionRepository
import common.ApplicationExceptions
import common.processError
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


private val tokenizer = LoginInteractor.KtorJWT(SECRET_JWT)

fun main() {
    initDB()
    embeddedServer(Netty, 8090) {

        install(Koin) { modules(mainModule) }

        val tokenizer = LoginInteractor.KtorJWT(SECRET_JWT)

        install(Authentication) {
            jwt {
                verifier(tokenizer.verifier)
                validate {
                    UserIdPrincipal(it.payload.getClaim("name").asString())
                }
            }
        }

        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }

        install(CORS) {
            method(HttpMethod.Options)
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.Authorization)
            allowCredentials = true
            anyHost()
        }

        install(StatusPages) {
            exception<ApplicationExceptions> { call.processError(it) }
        }

        routing { installRoutes() }
    }.start(wait = true)
}

private fun Routing.installRoutes() {
    val questionRepository: QuestionRepository by inject()
    val jobRepository: JobRepository by inject()

    get("/") {
        call.respondText(APP_NAME, contentType = ContentType.Text.Plain)
    }

    route("/questions") {
        get {
            val quantity = call.parameters.getOrFail<Int>(QUESTIONS_QUANTITY)
            val tagId = call.parameters.getOrFail<Int>(TAG)

            if (tagId == 0) {
                call.respondText((questionRepository.getInterviewQuestions(quantity)))
            } else {
                call.respondText((questionRepository.getInterviewQuestionsByTag(tagId, quantity)))
            }
        }
    }

    route("/tags") {
        get {
            call.respondText(questionRepository.getTags())
        }
    }

    route("/jobs") {
        get {
            call.respondText(jobRepository.getJobs())
        }
    }
}

private suspend fun ApplicationCall.respondCustom(questions: Any) {
    respond(mapOf("questions" to questions))
}

private fun initDB() {
    Database.connect(DB_URL, DB_DRIVER)
}