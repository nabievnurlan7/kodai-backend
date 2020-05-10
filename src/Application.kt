package com.futuris

import com.fasterxml.jackson.databind.SerializationFeature
import com.futuris.di.mainModule
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


private val tokenizer = LoginInteractor.KtorJWT(SECRET_JWT)


fun main() {
    initDB()
    embeddedServer(Netty, 8080) {

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
    }
}

private fun Routing.installRoutes() {
    val loginInteractor = LoginInteractor()
    val dataInteractor: DummyDataInteractor by inject()


    get("/") {
        call.respondText(APP_NAME, contentType = ContentType.Text.Plain)
    }

    get("/html") {
        call.respondText(dataInteractor.getHtml(), contentType = ContentType.Text.Html)
    }

    post("/login") {
        val post = call.receive<LoginInteractor.LoginRegister>()

        if (loginInteractor.checkCredentials(post)) {
            call.respondRedirect("/", permanent = false)
        } else {
            throw ApplicationExceptions.InvalidCredentialsException()
        }
    }

    route("/data") {
        get {
            call.respond(mapOf("data" to synchronized(dataInteractor.getDataList()) {
                dataInteractor.getDataList().toList()
            }))
        }

        authenticate {
            post {
                val post = call.receive<DummyDataInteractor.PostData>()
                dataInteractor.putData(post.data.text)
                call.respond(mapOf("OK" to true))
            }
        }
    }
}


private suspend fun ApplicationCall.respondCustom(questions: Any) {
    respond(mapOf("questions" to questions))
}

private fun initDB() {
    Database.connect(DB_URL, DB_DRIVER)
}