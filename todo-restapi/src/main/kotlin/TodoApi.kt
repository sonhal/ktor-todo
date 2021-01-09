package no.sonhal

import TodoItem
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.ContentType.Text.Plain
import io.ktor.response.*
import io.ktor.routing.*
import java.time.LocalDate

fun Routing.todoApi() {
    route("/api") {
        get("/todos") {
            call.respond(todos)
        }

        get("/todos/{id}") {
            val id = call.parameters["id"]
            try {
                call.respond(todos[id?.toInt() ?: 0])
            } catch (e: IndexOutOfBoundsException) {
                call.respond(HttpStatusCode.NotFound, "Nothing here")
            }
        }
        post("/todos") {
            call.respond(HttpStatusCode.MethodNotAllowed)
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