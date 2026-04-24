package pt.unl.fct.iadi.novaevents.client

import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import pt.unl.fct.iadi.novaevents.controller.dto.response.WeatherResponse

@HttpExchange
interface OpenWeatherClient {
    @GetExchange("/data/2.5/weather")
    fun getWeather(
        @RequestParam q: String,
        @RequestParam appid: String,
        @RequestParam units: String,
        @RequestHeader("Authorization") authorization: String,
    ): WeatherResponse
}