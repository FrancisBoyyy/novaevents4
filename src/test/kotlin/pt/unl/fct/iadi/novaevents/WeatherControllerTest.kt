package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pt.unl.fct.iadi.novaevents.controller.WeatherController
import pt.unl.fct.iadi.novaevents.service.WeatherService

class WeatherControllerTest {

    private val weatherService: WeatherService = mock(WeatherService::class.java)
    private val mvc = MockMvcBuilders.standaloneSetup(WeatherController(weatherService)).build()

    @Test
    fun `GET api-weather json returns raining true when service says it is raining`() {
        `when`(weatherService.isRaining("Lisbon")).thenReturn(true)

        mvc.perform(
            get("/api/weather")
                .param("location", "Lisbon")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("""{"raining":true}"""))
    }

    @Test
    fun `GET api-weather json returns raining false when service says it is clear`() {
        `when`(weatherService.isRaining("Lisbon")).thenReturn(false)

        mvc.perform(
            get("/api/weather")
                .param("location", "Lisbon")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("""{"raining":false}"""))
    }

    @Test
    fun `GET api-weather html returns the weather fragment view`() {
        `when`(weatherService.isRaining("Lisbon")).thenReturn(true)

        mvc.perform(
            get("/api/weather")
                .param("location", "Lisbon")
                .accept(MediaType.TEXT_HTML)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/weather :: result"))
            .andExpect(model().attribute("raining", true))
    }
}
