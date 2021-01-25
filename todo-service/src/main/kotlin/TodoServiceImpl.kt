
@file:Suppress("PackageDirectoryMismatch")
package no.sonhal.todo.service

import no.sonhal.todo.shared.TodoItem
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import no.sonhal.todo.shared.Importance
import java.time.LocalDate


class TodoServiceImpl : TodoService {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
    }
    val apiEndpoint = "http://localhost:8090/api/todos"

    override fun update(id: Int, todo: TodoItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadUserData(userId: String): UserData {
        return UserData(backgroundColor = "red")
    }

    override fun create(todo: TodoItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAll(): List<TodoItem> {
        return client.get(apiEndpoint)
        //return listOf(TodoItem(1, "hello", "details m8", "Me", LocalDate.now(), Importance.HIGH))
    }

    override fun getTodo(id: Int): TodoItem {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}