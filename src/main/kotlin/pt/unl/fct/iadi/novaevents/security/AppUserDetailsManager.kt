package pt.unl.fct.iadi.novaevents.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import pt.unl.fct.iadi.novaevents.domain.AppUser
import pt.unl.fct.iadi.novaevents.domain.enums.AppRole
import pt.unl.fct.iadi.novaevents.domain.enums.Roles
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository

class AppUserDetailsManager(private val userRepository: AppUserRepository, private val passwordEncoder: PasswordEncoder) : UserDetailsManager {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: AppUser = userRepository.findByUsername(username) ?:
        throw UsernameNotFoundException(username)
        return User(
            user.username, user.password,
            user.roles.map { SimpleGrantedAuthority(it.role?.name) })
    }

    override fun createUser(user: UserDetails?) {
        requireNotNull(user) { "UserDetails cannot be null" }

        if (userRepository.existsByUsername(user.username)) {
            throw IllegalArgumentException("User already exists")
        }

        val encodedPassword = passwordEncoder.encode(user.password)

        val appUser = AppUser(
            username = user.username,
            password = encodedPassword
        )

        val roles = user.authorities.map {
            val roleEnum = Roles.valueOf(it.authority)

            AppRole(
                role = roleEnum,
                user = appUser
            )
        }.toMutableSet()

        appUser.roles = roles

        userRepository.createUser(appUser)
    }

    override fun updateUser(user: UserDetails?) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(username: String?) {
        TODO("Not yet implemented")
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {
        TODO("Not yet implemented")
    }

    override fun userExists(username: String): Boolean {
        return userRepository.existsByUsername(username)
        //DO
    }
}