package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.MediaType
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.novaevents.controller.dto.request.RegisterForm

@Controller
@RequestMapping(path = arrayOf("/"), produces = [(MediaType.TEXT_HTML_VALUE)])
class AuthController(private val userDetailsManager: UserDetailsManager) {
    @GetMapping("/login")
    fun login(): String {
        return "auth/login"
    }

    /**
    @GetMapping("/register")
    fun registerForm(model: Model): String {
        model.addAttribute("user", RegisterForm())
        return "auth/register"
    }

    @PostMapping("/register")
    fun register(
        @ModelAttribute user: RegisterForm,
        model: Model
    ): String {
        try {
            val springUser = org.springframework.security.core.userdetails.User
                .withUsername(user.username)
                .password(user.password)
                .roles("EDITOR")
                .build()

            userDetailsManager.createUser(springUser)

            return "redirect:/login"

        } catch (e: Exception) {
            model.addAttribute("error", e.message)
            return "auth/register"
        }
    }
    */
}