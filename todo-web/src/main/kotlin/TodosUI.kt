package no.sonhal.todo.web

import io.ktor.application.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import no.sonhal.todo.shared.Importance
import no.sonhal.todo.shared.TodoItem
import no.sonhal.todo.web.viewmodels.TodoVM
import java.time.LocalDate


val todo = TodoItem(
    1,
    "Add database processing",
    "Add backed support to this code",
    "Kevin",
    LocalDate.of(2021, 1, 18),
    Importance.HIGH
)

val todos = listOf(todo, todo)


fun Routing.todos() {
    get("/todos") {
        call.respond(
            MustacheContent("todos.hbs", mapOf("todos" to TodoVM(todos)))
        )
    }
}