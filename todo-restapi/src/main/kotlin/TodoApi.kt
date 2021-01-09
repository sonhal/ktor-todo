package no.sonhal

import TodoItem
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.LocalDate

fun Routing.todoApi() {
    route("/api/todo") {
        get("/") {
            call.respond(todos)
        }
    }
}

val todo1 = TodoItem(
    "Add RestAPI Data access",
    "Add database support",
    "Me",
    LocalDate.of(2021, 1, 9),
    Importance.MEDIUM
)

val todo2 = TodoItem(
    "Add RestAPI Data Service",
    "Add a service to get the data",
    "Me",
    LocalDate.of(2021, 1, 9),
    Importance.HIGH
)