package no.sonhal.todo.web

import com.auth0.jwt.JWT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import no.sonhal.todo.service.TodoService
import no.sonhal.todo.shared.Importance
import no.sonhal.todo.shared.TodoItem
import no.sonhal.todo.web.viewmodels.TodoVM
import java.time.LocalDate


fun Routing.todos(todoService: TodoService) {
    authenticate(oauthAuthentication) {

        get("/todos") {
            val subject = getSubject()
            val log = java.util.logging.Logger.getLogger("Router")
            log.info("subject of call: $subject")
            call.respond(
                MustacheContent("todos.hbs", mapOf("todos" to TodoVM(todoService.getAll(), todoService.loadUserData(subject).backgroundColor)))
            )
        }
    }
}


private fun PipelineContext<Unit, ApplicationCall>.getSubject(): String {
    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
    println("token type: " + principal?.tokenType)
    val jwt = JWT.decode(principal?.accessToken)
    return jwt.subject
}