package no.sonhal.todo

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.jackson.*
import no.sonhal.dataaccess.shared.TodoService
import no.sonhal.dataaccess.shared.TodoServiceImpl
import no.sonhal.todolist.TodoListRepositorySql


fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

val todoContentV1 = ContentType("application", "vnd.todoapi.v1+json")

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    moduleWithDependencies(TodoServiceImpl(TodoListRepositorySql()))
}

fun Application.moduleWithDependencies(service: TodoService) {
    install(Routing) {
        todoApi(service)
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
