package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoggingInterceptor() : HandlerInterceptor {
    private val log = LoggerFactory.getLogger(LoggingInterceptor::class.java)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val principal = request.userPrincipal?.name ?: "anonymous"
        log.info("[{}] {} {} [{}]", principal, request.method, request.requestURI, response.status)
    }
}