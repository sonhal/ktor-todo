package no.sonhal.todo.web.viewmodels

import no.sonhal.todo.shared.TodoItem

data class TodoVM(private val todos: List<TodoItem>, val background: String) {
    val todoItems = todos
    val userName = "Sondre"
}