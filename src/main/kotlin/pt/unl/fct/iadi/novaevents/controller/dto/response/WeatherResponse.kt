package pt.unl.fct.iadi.novaevents.controller.dto.response

data class WeatherResponse(
    val name: String,
    val weather: List<WeatherCondition>
)