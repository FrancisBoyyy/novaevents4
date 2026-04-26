package pt.unl.fct.iadi.novaevents.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.client.OpenWeatherClient

@Service
class WeatherService(
    private val openWeatherClient: OpenWeatherClient,
    @Value("\${weather.api.key}") private val apiKey: String
    ) {

    /**
    fun isRaining(location:String):Boolean {
        val weather = openWeatherClient.getWeather(
            q = location, appid = apiKey, units = "metric"
        );
        return false;
    }
    */
    fun isRaining(location: String): Boolean? {
        return try {
            val weather = openWeatherClient.getWeather(q = location, appid = apiKey, units = "metric")
            weather.weather.firstOrNull()?.main?.equals("Rain", ignoreCase = true) == true
        } catch (e: Exception) {
            null
        }
    }
}