package no.sonhal.todo.web

import io.ktor.application.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import no.sonhal.todo.service.TodoService
import no.sonhal.todo.shared.Importance
import no.sonhal.todo.shared.TodoItem
import no.sonhal.todo.web.viewmodels.TodoVM
import java.time.LocalDate


fun Routing.todos(todoService: TodoService) {
    get("/todos") {
        call.respond(
            MustacheContent("todos.hbs", mapOf("todos" to TodoVM(todoService.getAll())))
        )
    }
}