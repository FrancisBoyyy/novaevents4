package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import pt.unl.fct.iadi.novaevents.domain.AppUser
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository
import pt.unl.fct.iadi.novaevents.security.AppUserDetailsManager
import pt.unl.fct.iadi.novaevents.service.NovaEventsService
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class ServiceTests {
    @Mock
    lateinit var repo: AppUserRepository
    @InjectMocks
    lateinit var service: NovaEventsService
    @InjectMocks
    lateinit var userService: AppUserDetailsManager
    @Test
    fun `getByUsername returns user when found`() {
        `when`(repo.findByUsername("alice"))
            .thenReturn(AppUser(id = 1, username = "alice"))
            val result = userService.loadUserByUsername("alice")
        assertEquals("alice", result.username)
    }
}