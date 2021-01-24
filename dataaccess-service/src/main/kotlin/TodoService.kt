package no.sonhal.dataaccess.shared

import no.sonhal.todo.shared.TodoItem

interface TodoService {
    fun getAll(): List<TodoItem>
    fun getTodo(id: Int): TodoItem
    fun delete(id: Int): Boolean
    fun create(todo: TodoItem): Boolean
    fun update(id: Int, todo: TodoItem): Boolean
}