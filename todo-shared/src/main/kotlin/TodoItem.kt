package no.sonhal.todo.shared

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import java.time.LocalDate

public data class TodoItem (
    val id: Int,
    val title: String,
    val details: String,
    val assignedTo: String,
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = ToStringSerializer::class)
    val dueDate: LocalDate,
    val importance: Importance
)

public enum class Importance {
    LOW, MEDIUM, HIGH
}
