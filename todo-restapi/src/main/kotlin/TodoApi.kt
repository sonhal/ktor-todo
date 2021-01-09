package no.sonhal

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.todoApi() {
    route("/api/todo") {
        get("/") {
            call.respond(todos)
        }
    }
}