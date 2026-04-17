package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.unl.fct.iadi.novaevents.domain.AppUser

interface AppUserRepository : JpaRepository<AppUser, Long> {
    fun createUser(user: AppUser): AppUser
    fun findByUsername(username: String): AppUser?
    fun existsByUsername(username: String): Boolean
}