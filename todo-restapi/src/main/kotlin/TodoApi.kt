package no.sonhal

import TodoItem
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.ContentType.Text.Plain
import io.ktor.request.*
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
            val todo = call.receive<TodoItem>()
            val newTodo =
                TodoItem(todos.size + 1, todo.title, todo.details, todo.assignedTo, todo.dueDate, todo.importance)
            todos = todos + newTodo

            call.respond(HttpStatusCode.Created, todos)
        }

        put("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            val foundItem = todos.getOrNull(id.toInt())

            if(foundItem == null){
                call.respond(HttpStatusCode.NotFound)
                return@put
            }

            val todo = call.receive<TodoItem>()

            todos = todos.filter { it.id == todo.id }
            todos = todos + todo

            call.respond(HttpStatusCode.NoContent)
        }
    }
}

val todo1 = TodoItem(
    1,
    "Add RestAPI Data access",
    "Add database support",
    "Me",
    LocalDate.of(2021, 1, 9),
    Importance.MEDIUM
)

val todo2 = TodoItem(
    2,
    "Add RestAPI Data Service",
    "Add a service to get the data",
    "Me",
    LocalDate.of(2021, 1, 9),
    Importance.HIGH
)