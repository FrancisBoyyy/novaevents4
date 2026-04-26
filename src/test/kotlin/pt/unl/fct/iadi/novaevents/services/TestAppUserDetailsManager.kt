package pt.unl.fct.iadi.novaevents.services

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

import pt.unl.fct.iadi.novaevents.domain.AppUser
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository
import pt.unl.fct.iadi.novaevents.security.AppUserDetailsManager

@ExtendWith(MockitoExtension::class)
class TestAppUserDetailsManager {

    @Mock
    lateinit var appUserRepository: AppUserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    lateinit var userService: AppUserDetailsManager

    // modificação mentirosa

    @Test
    fun getByUsernameReturnsUserWhenFound() {
        `when`(appUserRepository.findByUsername("alice"))
            .thenReturn(AppUser(id = 1, username = "alice"))

        val result = userService.loadUserByUsername("alice")

        assertEquals("alice", result.username)
    }
}