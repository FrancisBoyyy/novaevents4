package pt.unl.fct.iadi.novaevents.controller.dto.response

import pt.unl.fct.iadi.novaevents.domain.enums.ClubCategory

data class DetailedClubResponse(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory,
    val events: List<EventResponse>
) {
}