package pt.unl.fct.iadi.novaevents.controller.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import pt.unl.fct.iadi.novaevents.domain.enums.EventType
import java.time.LocalDate

data class EventForm(
    @field:NotBlank
    val name: String? = null,

    @field:NotNull
    val date: LocalDate? = null,

    val location: String? = null,

    @field:NotNull
    val type: EventType? = null,

    val description: String? = null,
) {
}