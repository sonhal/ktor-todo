package no.sonhal

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.jackson.*


fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

val todoContentV1 = ContentType("application", "vnd.todoapi.v1+json")
var todos = listOf(todo1, todo2)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Routing) {
        trace { application.log.trace(it.buildText()) }
        todoApi()
    }
    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain)
            throw e
        }
    }
    install(ContentNegotiation) {

        jackson(todoContentV1) {
            enable(SerializationFeature.INDENT_OUTPUT)
            deactivateDefaultTyping()
        }

        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}
