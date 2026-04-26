package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import pt.unl.fct.iadi.novaevents.domain.AppUser
import pt.unl.fct.iadi.novaevents.domain.Event
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.security.EventSecurity
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class TestEventSecurity {

    @org.mockito.Mock
    private lateinit var eventRepository: EventRepository

    private fun authentication(username: String) =
        UsernamePasswordAuthenticationToken(
            username,
            null,
            listOf(SimpleGrantedAuthority("ROLE_EDITOR"))
        )

    @Test
    fun `isOwner returns true when authenticated user owns the event`() {
        val event = org.mockito.Mockito.mock(Event::class.java)
        val owner = org.mockito.Mockito.mock(AppUser::class.java)

        `when`(owner.username).thenReturn("teacher")
        `when`(event.owner).thenReturn(owner)
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))

        val security = EventSecurity(eventRepository)

        assertTrue(security.isOwner(1L, authentication("teacher")))
    }

    @Test
    fun `isOwner returns false when authenticated user does not own the event`() {
        val event = org.mockito.Mockito.mock(Event::class.java)
        val owner = org.mockito.Mockito.mock(AppUser::class.java)

        `when`(owner.username).thenReturn("someone-else")
        `when`(event.owner).thenReturn(owner)
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))

        val security = EventSecurity(eventRepository)

        assertFalse(security.isOwner(1L, authentication("teacher")))
    }

    @Test
    fun `isOwner returns false when event does not exist`() {
        `when`(eventRepository.findById(1L)).thenReturn(Optional.empty())

        val security = EventSecurity(eventRepository)

        assertFalse(security.isOwner(1L, authentication("teacher")))
    }
}
