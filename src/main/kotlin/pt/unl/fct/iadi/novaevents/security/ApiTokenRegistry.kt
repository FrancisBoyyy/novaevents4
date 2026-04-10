package pt.unl.fct.iadi.bookstore.security

import org.springframework.stereotype.Component

@Component
class ApiTokenRegistry {
    val tokenToApp = mapOf(
        "token-catalog-abc123" to "catalog-app",
        "token-mobile-def456" to "mobile-app",
        "token-web-ghi789" to "web-app"
    )
}