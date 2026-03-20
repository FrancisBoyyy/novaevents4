package pt.unl.fct.iadi.novaevents.domain

import pt.unl.fct.iadi.novaevents.domain.enums.EventType
import java.time.LocalDate

class Event(
    val id: Long,
    val clubId: Long,
    val name: String,
    val date: LocalDate,
    val location: String,
    val type: EventType,
    val description: String
) {
}