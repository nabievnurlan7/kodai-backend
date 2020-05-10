package com.futuris

import com.fasterxml.jackson.databind.SerializationFeature
import com.futuris.answers.AnswersInteractor
import com.futuris.codes.CodeInteractor
import com.futuris.questions.QuestionsInteractor
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database

private val questionsInteractor = QuestionsInteractor()
private val answersInteractor = AnswersInteractor()
private val codeInteractor = CodeInteractor()

private val tokenizer = LoginInteractor.KtorJWT(SECRET_JWT)


fun main() {
    initDB()
    embeddedServer(Netty, 8080) {

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

        routing {
            get("/") {
                call.respondText(APP_NAME)
            }

            get("/checkCode") {
                val code = call.parameters[CODE_PARAM]
                code?.let { call.respondText(codeInteractor.checkCode(it)) }
            }

            get("/getFirstTest") {
                val lang = call.parameters[LANG_PARAM]
                lang?.let { call.respondText(questionsInteractor.getFirstTestQuestions(it)) }
            }

            get("/getSecondTest") {
                val lang = call.parameters[LANG_PARAM]
                lang?.let { call.respondText(questionsInteractor.getSecondTestQuestions(it)) }
            }

            get("/getThirdTest") {
                val lang = call.parameters[LANG_PARAM]
                lang?.let { call.respondText(questionsInteractor.getThirdTestQuestions(it)) }
            }

            get("/getFourthTest") {
                val lang = call.parameters[LANG_PARAM]
                lang?.let { call.respondText(questionsInteractor.getFourthTestQuestions(it)) }
            }

            post("/sendAnswers") {
                val jsonAnswers = call.receive<String>()
                call.respondText(answersInteractor.processAnswers(jsonAnswers))
            }
        }
    }.start(wait = true)
}

private fun initDB() {
    Database.connect(DB_URL, DB_DRIVER)
}