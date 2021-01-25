package no.sonhal.todo.web

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.mustache.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.sonhal.todo.service.TodoService
import no.sonhal.todo.service.TodoServiceImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import io.github.cdimascio.dotenv.dotenv


val todoWebModule = module {
    single<TodoService> { TodoServiceImpl() }
}

const val oauthAuthentication = "oauthAuthentication"

val dotenv = dotenv()


val loginProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = dotenv["name"],
    authorizeUrl = dotenv["authorizeUrl"],
    accessTokenUrl = dotenv["accessTokenUrl"],
    requestMethod = HttpMethod.Post,
    clientId = dotenv["clientId"],
    clientSecret = dotenv["clientSecret"],
    defaultScopes = listOf("todoAPI.read", "todoAPI.write"),
)

fun main(args: Array<String>) {
    println("Running webpage server")
    startKoin {
        modules(todoWebModule)
    }
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

@Suppress("unused")
fun Application.module() {

    val oauthHttpClient: HttpClient = HttpClient().apply {
        environment.monitor.subscribe(ApplicationStopping){
            close()
        }
    }

    val todoService: TodoService by inject()
    moduleWithDependencies(todoService, oauthHttpClient)
}

fun Application.moduleWithDependencies(service: TodoService, httpClient: HttpClient) {
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

    install(Authentication) {
        oauth(oauthAuthentication) {
            providerLookup = {
                loginProvider
            }
            client = httpClient
            urlProvider = {redirectUrl("/todos")}
        }
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

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host() + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}