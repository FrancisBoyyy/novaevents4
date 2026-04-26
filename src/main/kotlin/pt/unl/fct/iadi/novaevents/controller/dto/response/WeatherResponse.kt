package pt.unl.fct.iadi.novaevents.controller.dto.response

data class WeatherResponse(
    val name: String? = null,
    val weather: List<WeatherCondition> = emptyList()
)