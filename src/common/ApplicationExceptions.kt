package common

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

sealed class ApplicationExceptions(message: String) : RuntimeException(message) {
    class InvalidCredentialsException(message: String = "Invalid credentials") : ApplicationExceptions(message)
}

suspend fun ApplicationCall.processError(exception: ApplicationExceptions) {
    val errorMap = mapOf("OK" to false, "error" to (exception.message ?: ""))

    when (exception) {
        is ApplicationExceptions.InvalidCredentialsException -> respond(HttpStatusCode.Unauthorized, errorMap)
        else -> respond(HttpStatusCode.Forbidden)
    }
}