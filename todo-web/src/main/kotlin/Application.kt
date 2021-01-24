package no.sonhal.todo.web

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.application.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.sonhal.todo.service.TodoService
import no.sonhal.todo.service.TodoServiceImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.ext.inject


val todoWebModule = module {
    single<TodoService> { TodoServiceImpl() }
}

fun main(args: Array<String>) {
    println("Running webpage server")
    startKoin {
        modules(todoWebModule)
    }
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

@Suppress("unused")
fun Application.module() {
    val todoService: TodoService by inject()
    moduleWithDependencies(todoService)
}

fun Application.moduleWithDependencies(service: TodoService) {
    install(StatusPages) {
        when {
            isDev -> {
                this.exception<Throwable> { e ->
                    call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
                    throw e
                }
            }
            isTest -> {
                this.exception<Throwable> { e ->
                    call.response.status(HttpStatusCode.InternalServerError)
                    throw e
                }
            }
            isProd -> {
                this.exception<Throwable> { e ->
                    call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
                    throw e
                }
            }
        }
        status(HttpStatusCode.NotFound) {
            call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
        }
    }

    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates")
    }

    install(Routing) {
        if (isDev) {
            trace { application.log.trace(it.buildText()) }
        }
        todos(service)
        staticResources()
    }
}

val Application.envKind get() = environment.config.property("ktor.environment").getString()
val Application.isDev get() = envKind == "dev"
val Application.isTest get() = envKind == "test"
val Application.isProd get() = !isDev && !isTest