package no.sonhal.todo

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.SerializationFeature
import io.github.cdimascio.dotenv.dotenv
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
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
import java.util.concurrent.TimeUnit

val todoContentV1 = ContentType("application", "vnd.todoapi.v1+json")

val todoAppModule = module {
    single<TodoService> { TodoServiceImpl(get())}
    single<TodoListRepository> { TodoListRepositorySql() }
}

val dotenv = dotenv()
val jwksUrl = dotenv["jwksUrl"]
val jwtIssuer = dotenv["jwtIssuer"]
val jwtAudience = dotenv["jwtAudience"]
val jwtRealm = dotenv["jwtRealm"]

val jwkProvider = JwkProviderBuilder(jwksUrl)
    .cached(10, 24, TimeUnit.HOURS)
    .rateLimited(10, 1, TimeUnit.MINUTES)
    .build()

val log = java.util.logging.Logger.getLogger("Main")

fun main(args: Array<String>): Unit {
    startKoin {
        modules(todoAppModule)
    }
    io.ktor.server.cio.EngineMain.main(args)
}


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
   val todoService: TodoService by inject()
    moduleWithDependencies(todoService)
}

fun Application.moduleWithDependencies(service: TodoService) {
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

    install(Authentication) {
        jwt("jwt") {
            realm = jwtRealm
            verifier(jwkProvider, jwtIssuer)

            var cred: JWTCredential

            validate { credential ->
                cred = credential
                log.debug("Credentials audience: ${credential.payload.audience}")
                log.debug("audience: ${jwtAudience}")
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
    install(Routing) {
        todoApi(service)
    }
}