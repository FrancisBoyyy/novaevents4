package pt.unl.fct.iadi.bookstore.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiTokenFilter(val apiTokenRegistry : ApiTokenRegistry) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val uri = request.requestURI
        if(uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui")){
            filterChain.doFilter(request, response)
            return
        }

        val token = request.getHeader("X-Api-Token")

        val isValidAuth = (!(token==null)) && (apiTokenRegistry.tokenToApp[token] != null)

        // get auth info from request and validate...
        if (!isValidAuth) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("""{"error":"UNAUTHORIZED","message":"Missing or invalid X-Api-Token"}""")
            return
        }
        filterChain.doFilter(request, response)
    }
}