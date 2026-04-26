package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.service.NovaEventsService
import pt.unl.fct.iadi.novaevents.controller.dto.request.EventForm
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.service.WeatherService
import java.security.Principal
import java.time.LocalDate

@Controller
@RequestMapping(path = arrayOf("/"), produces = [(MediaType.TEXT_HTML_VALUE)])
class NovaEventsController(
    private val service: NovaEventsService,
    private val weatherService: WeatherService,
) {
    private val OUTDOOR_CLUB_NAME = "Hiking & Outdoors Club"

    @GetMapping("/clubs")
    fun listClubs(model: Model): String {

        val clubs = service.getAllClubs()

        model.addAttribute("clubs", clubs)

        return "clubs/list"
    }

    @GetMapping("/clubs/{id}")
    fun clubDetails(@PathVariable id: Long, model: Model): String {

        val club = service.getClubDetails(id)

        model.addAttribute("club", club)

        return "clubs/detail"
    }

    @GetMapping("/events")
    fun listEvents(
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) clubId: Long?,
        @RequestParam(required = false) from: LocalDate?,
        @RequestParam(required = false) to: LocalDate?,
        model: Model
    ): String {

        val events = service.getAllEvents(type, clubId, from, to)
        val clubs = service.getAllClubs()
        val eventTypes = service.getEventTypes()

        model.addAttribute("events", events)
        model.addAttribute("clubs", clubs)
        model.addAttribute("eventTypes", eventTypes)

        return "events/list"
    }

    @GetMapping("/clubs/{clubId}/events/{eventId}")
    fun eventDetails(@PathVariable clubId: Long, @PathVariable eventId: Long, model: ModelMap): String {

        //model["event"] = service.getEventDetails(id)
        val event = service.getEventDetails(clubId, eventId)

        model.addAttribute("event", event)

        return "events/detail"
    }

    @GetMapping("/clubs/{clubId}/events/create")
    fun createEvent(@PathVariable clubId: Long, model: Model): String{
        val eventTypes = service.getEventTypes()
        model.addAttribute("event", EventForm())
        model.addAttribute("clubId", clubId)
        model.addAttribute("eventTypes", eventTypes)
        return "events/create"
    }

    @PostMapping("/clubs/{id}/events")
    fun createEvent(
        @PathVariable id: Long,
        @Valid @ModelAttribute("event") event: EventForm,
        bindingResult: BindingResult,
        model: Model,
        principal: Principal
    ): String {

        val club = service.getClubDetails(id)

        validateOutdoorEvent(club.name, event.location, bindingResult, weatherService)

        if (bindingResult.hasErrors()) {
            model.addAttribute("clubId", id)
            return "events/create"
        }

        try {
            val eventCreated = service.createEvent(id, event, principal.name);
            return "redirect:/clubs/${id}/events/${eventCreated.id}"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "duplicate.name", e.message ?: "An event with this name already exists")
            model.addAttribute("clubId", id)
            model.addAttribute("event", event)
            val eventTypes = service.getEventTypes()
            model.addAttribute("eventTypes", eventTypes)
            return "events/create"
        }
    }

    @GetMapping("/clubs/{clubId}/events/{eventId}/edit")
    @PreAuthorize("@eventSecurity.isOwner(#eventId, authentication)")
    fun editEvent(
        @PathVariable clubId: Long,
        @PathVariable eventId: Long,
        model: Model
    ): String{

        var event = service.getEventDetails(clubId, eventId)

        val eventTypes = service.getEventTypes()

        var eventForm = EventForm(
            name = event.name,
            date = event.date,
            location = event.location,
            type = event.type,
            description = event.description
        )

        model.addAttribute("event", eventForm)
        model.addAttribute("clubId", clubId)
        model.addAttribute("eventId", eventId)
        model.addAttribute("eventTypes", eventTypes)

        return "events/edit"
    }

    @PutMapping("/clubs/{clubId}/events/{eventId}")
    fun editEvent(
        @PathVariable clubId: Long,
        @PathVariable eventId: Long,
        @Valid @ModelAttribute("event") event: EventForm,
        bindingResult: BindingResult,
        model: ModelMap
    ): String {

        val club = service.getClubDetails(clubId)

        validateOutdoorEvent(club.name, event.location, bindingResult, weatherService)

        if (bindingResult.hasErrors()) {
            model.addAttribute("clubId", clubId)
            model.addAttribute("eventId", eventId)
            return "events/edit"
        }

        try {
            val updated = service.editEvent(clubId, eventId, event)
            return "redirect:/clubs/${updated.clubId}/events/${updated.id}"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "duplicate.name", e.message ?: "An event with this name already exists")
            model.addAttribute("clubId", clubId)
            model.addAttribute("eventId", eventId)
            model.addAttribute("event", event)
            val eventTypes = service.getEventTypes()
            model.addAttribute("eventTypes", eventTypes)
            return "events/edit"
        }
    }

    @GetMapping("/clubs/{clubId}/events/{eventId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteEvent(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String{

        var event = service.getEventDetails(clubId, eventId)

        model.addAttribute("event", event)

        return "events/confirm-delete"
    }

    @DeleteMapping("/clubs/{clubId}/events/{eventId}")
    fun deleteEvent(@PathVariable clubId: Long, @PathVariable eventId: Long): String {
        var clubId = service.deleteEvent(clubId, eventId);

        return "redirect:/clubs/${clubId}"
    }

    private fun validateOutdoorEvent(
        clubName: String,
        location: String?,
        bindingResult: BindingResult,
        weatherService: WeatherService
    ) {
        if (clubName != OUTDOOR_CLUB_NAME) return

        if (location.isNullOrBlank()) {
            bindingResult.rejectValue(
                "location",
                "location.required.outdoor",
                "Location is required for outdoor events"
            )
            return
        }

        val trimmedLocation = location.trim()
        if (weatherService.isRaining(trimmedLocation)) {
            bindingResult.rejectValue(
                "location",
                "weather.raining",
                "It is currently raining at \"$trimmedLocation\" — outdoor events cannot be created in bad weather"
            )
        }
    }
}