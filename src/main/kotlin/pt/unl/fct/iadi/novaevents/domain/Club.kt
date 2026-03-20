package pt.unl.fct.iadi.novaevents.domain

import pt.unl.fct.iadi.novaevents.domain.enums.ClubCategory

class Club(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory
) {
}