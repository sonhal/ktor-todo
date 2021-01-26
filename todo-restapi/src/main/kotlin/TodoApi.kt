package no.sonhal.todo

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import no.sonhal.dataaccess.shared.TodoService
import no.sonhal.todo.shared.TodoItem
import java.util.*
import kotlin.random.Random.Default.nextInt

fun Routing.todoApi(service: TodoService) {
    authenticate("jwt") {
        route("/api") {

            accept(todoContentV1) {
                get("/todos") {
                    call.respond(service.getAll())
                }
            }

            get("/todos") {
                call.respond(service.getAll())
            }

            get("/todos/{id}") {
                val id = call.parameters["id"]
                try {
                    call.respond(service.getTodo(id?.toInt() ?: 0))
                } catch (e: IndexOutOfBoundsException) {
                    call.respond(HttpStatusCode.NotFound, "Nothing here")
                }
            }
            post("/todos") {
                val todo = call.receive<TodoItem>()
                val response = service.create(
                    TodoItem(
                        nextInt(),
                        todo.title,
                        todo.details,
                        todo.assignedTo,
                        todo.dueDate,
                        todo.importance
                    )
                )
                call.respond(HttpStatusCode.Created, response)
            }

            put("/todos/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }

                val todo = call.receive<TodoItem>()
                val response = service.update(todo.id, todo)
                if (!response) {
                    call.respond(HttpStatusCode.BadRequest)
                }

                call.respond(HttpStatusCode.NoContent)
            }

            delete("/todos/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                val response = service.delete(id.toInt())
                if (!response) {
                    call.respond(HttpStatusCode.BadRequest)
                }
                call.respond(HttpStatusCode.NoContent)

            }
        }
    }
}
