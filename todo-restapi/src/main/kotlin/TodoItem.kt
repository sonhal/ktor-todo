package no.sonhal

import java.time.LocalDate

data class TodoItem (
    val title: String,
    val details: String,
    val assignedTo: String,
    val dueDate: LocalDate,
    val importance: Int
)


val todo1 = TodoItem(
    "Add RestAPI Data access",
    "Add database support",
    "Me",
    LocalDate.of(2021, 1, 9),
    1
)

val todo2 = TodoItem(
    "Add RestAPI Data Service",
    "Add a service to get the data",
    "Me",
    LocalDate.of(2021, 1, 9),
    1
)