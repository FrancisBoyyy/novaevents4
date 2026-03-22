package pt.unl.fct.iadi.novaevents.controller.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import pt.unl.fct.iadi.novaevents.domain.enums.EventType
import java.time.LocalDate

data class EventForm(
    @field:NotBlank(message = "Name is required")
    val name: String = "",

    @field:NotNull(message = "Date is required")
    val date: LocalDate? = null,

    val location: String = "",

    @field:NotNull(message = "Event type is required")
    val type: EventType? = null,

    val description: String = "",
) {
}