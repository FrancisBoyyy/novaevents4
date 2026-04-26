package pt.unl.fct.iadi.novaevents.services

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

import pt.unl.fct.iadi.novaevents.domain.AppUser
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository
import pt.unl.fct.iadi.novaevents.security.AppUserDetailsManager

@ExtendWith(MockitoExtension::class)
class AppUserDetailsManagerTest {

    @Mock
    lateinit var repo: AppUserRepository

    @InjectMocks
    lateinit var userService: AppUserDetailsManager

    @Test
    fun getByUsernameReturnsUserWhenFound() {
        `when`(repo.findByUsername("alice"))
            .thenReturn(AppUser(id = 1, username = "alice"))

        val result = userService.loadUserByUsername("alice")

        assertEquals("alice", result.username)
    }
}