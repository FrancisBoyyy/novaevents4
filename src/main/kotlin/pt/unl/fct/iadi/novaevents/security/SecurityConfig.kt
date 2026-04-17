package pt.unl.fct.iadi.novaevents.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.savedrequest.CookieRequestCache
import pt.unl.fct.iadi.novaevents.repository.AppUserRepository
import pt.unl.fct.iadi.novaevents.security.filters.ApiTokenFilter
import pt.unl.fct.iadi.novaevents.security.filters.JwtCookieAuthFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(private val appUserRepository: AppUserRepository) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsManager(passwordEncoder: PasswordEncoder): UserDetailsManager = AppUserDetailsManager(appUserRepository, passwordEncoder)

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtCookieAuthFilter: JwtCookieAuthFilter, jwtAuthSuccessHandler: JwtAuthSuccessHandler): SecurityFilterChain {
        http
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .securityContext {
                it.securityContextRepository(RequestAttributeSecurityContextRepository())
            }
            .requestCache { it.requestCache(CookieRequestCache()) }
            .csrf { csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository())
                csrf.csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
            }
            .httpBasic {}
            .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/register", "/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/clubs", "/clubs/**", "/events", "/clubs/*/events/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/clubs/*/events").hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.PUT, "/clubs/*/events/**").hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.PATCH, "/clubs/*/events/**").hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.DELETE, "/clubs/*/events/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form.loginPage("/login").permitAll()
                form.successHandler(jwtAuthSuccessHandler)
            }
            .logout{ logout ->
                logout
                    .deleteCookies("JSESSIONID", "jwt")
                    .logoutSuccessHandler { _, response, _ -> response.status = 200 }
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint { _, response, _  ->
                        response.status = 401
                        response.contentType = "application/json"
                        response.writer.write("""{"error":"UNAUTHORIZED","message":"Authentication required"}""")
                    }
                    .accessDeniedHandler { _, response, _ ->
                        response.status = 403
                        response.contentType = "application/json"
                        response.writer.write("""{"error":"FORBIDDEN","message":"Access denied"}""")
                    }
            }
        return http.build()
    }
}