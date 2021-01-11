package no.sonhal


import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
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

            engine.application.module(true)

            with(engine) {
                it("should be OK to get the list of todos") {
                    handleRequest(HttpMethod.Get, "/api/todos").apply {
                        response.status().`should be`(HttpStatusCode.OK)
                    }
                }

                it("Should get the todos") {
                    handleRequest(HttpMethod.Get, "/api/todos").apply {
                        response.content
                            .shouldNotBeNull()
                            .shouldContain("database")
                    }
                }

                it("Should create todos") {
                    with(handleRequest(HttpMethod.Post, "/api/todos") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(mapper.writeValueAsString(todo1))
                    }) {
                        response.status().`should be`(HttpStatusCode.Created)
                    }
                }

                it("Should update the todo") {
                    with(handleRequest(HttpMethod.Put, "/api/todos/1") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(mapper.writeValueAsString(todo1))
                    }) {
                        response.status().`should be`(HttpStatusCode.NoContent)
                    }
                }

                it("Should delete the todo") {
                    with(handleRequest(HttpMethod.Put, "/api/todos/1") {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(mapper.writeValueAsString(todo1))
                    }) {
                        response.status().`should be`(HttpStatusCode.NoContent)
                    }
                }
            }
        }
    })