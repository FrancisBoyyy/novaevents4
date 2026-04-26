package pt.unl.fct.iadi.novaevents

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import pt.unl.fct.iadi.novaevents.controller.dto.response.WeatherResponse

class WeatherDtoDeserializationTest {

    private val mapper = jacksonObjectMapper()

    @Test
    fun `weather response can be deserialized when description is missing`() {
        val json = """
            {
              "name": "Lisbon",
              "weather": [
                { "main": "Clear" }
              ]
            }
        """.trimIndent()

        val response: WeatherResponse = mapper.readValue(json)

        assertEquals("Lisbon", response.name)
        assertEquals(1, response.weather.size)
        assertEquals("Clear", response.weather.first().main)
        assertNull(response.weather.first().description)
    }

    @Test
    fun `weather response can be deserialized when name is missing`() {
        val json = """
            {
              "weather": [
                { "main": "Rain", "description": "light rain" }
              ]
            }
        """.trimIndent()

        val response: WeatherResponse = mapper.readValue(json)

        assertNull(response.name)
        assertEquals("Rain", response.weather.first().main)
        assertEquals("light rain", response.weather.first().description)
    }
}
