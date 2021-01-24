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
import no.sonhal.todolist.TodoListRepository
import no.sonhal.todolist.TodoListRepositorySql
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.ext.inject


val todoAppModule = module {
    single<TodoService> { TodoServiceImpl(get())}
    single<TodoListRepository> { TodoListRepositorySql() }
}


fun main(args: Array<String>): Unit {
    startKoin {
        modules(todoAppModule)
    }
    io.ktor.server.cio.EngineMain.main(args)
}

val todoContentV1 = ContentType("application", "vnd.todoapi.v1+json")

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
   val todoService: TodoService by inject()
    moduleWithDependencies(todoService)
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
