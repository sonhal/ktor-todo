package no.sonhal.todo

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import no.sonhal.dataaccess.shared.TodoService
import no.sonhal.dataaccess.shared.todo1
import no.sonhal.dataaccess.shared.todo2
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object GetTodoSpec : Spek ({
        describe("Get the Todos") {
            val engine = TestApplicationEngine(createTestEnvironment())
            engine.start(wait = false)

            with(engine) {
                (environment.config as MapApplicationConfig).apply {
                    put("ktor.environment", "test")
                }
            }
            val mapper = jacksonObjectMapper()
                .registerModule(JavaTimeModule())

            val mockTodoService = mockk<TodoService>()
            beforeEachTest {
                clearMocks(mockTodoService)
            }

            engine.application.moduleWithDependencies(mockTodoService)

            with(engine) {
                it("should be OK to get the list of todos") {

                    every { mockTodoService.getAll() } returns listOf(todo1, todo2)


                    handleRequest(HttpMethod.Get, "/api/todos").apply {
                        response.status().`should be`(HttpStatusCode.OK)
                    }
                }

                it("Should get the todos") {

                    every { mockTodoService.getAll() } returns listOf(todo1, todo2)

                    handleRequest(HttpMethod.Get, "/api/todos").apply {
                        response.content
                            .shouldNotBeNull()
                            .shouldContain("database")
                    }
                }

                it("Should create todos") {

                    every { mockTodoService.create(any()) } returns true

                    with(handleRequest(HttpMethod.Post, "/api/todos") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(mapper.writeValueAsString(todo1))
                    }) {
                        response.status().`should be`(HttpStatusCode.Created)
                    }
                }

                it("Should update the todo") {

                    every { mockTodoService.update(any(), any()) } returns true

                    with(handleRequest(HttpMethod.Put, "/api/todos/1") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(mapper.writeValueAsString(todo1))
                    }) {
                        response.status().`should be`(HttpStatusCode.NoContent)
                    }
                }

                it("Should delete the todo") {

                    every { mockTodoService.delete(any()) } returns true

                    with(handleRequest(HttpMethod.Delete, "/api/todos/1") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(mapper.writeValueAsString(todo1))
                    }) {
                        response.status().`should be`(HttpStatusCode.NoContent)
                    }
                }
            }
        }
    })