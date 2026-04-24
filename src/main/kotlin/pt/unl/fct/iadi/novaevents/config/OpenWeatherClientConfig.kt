package pt.unl.fct.iadi.novaevents.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import pt.unl.fct.iadi.novaevents.client.OpenWeatherClient
import kotlin.jvm.java

@Configuration
class OpenWeatherClientConfig(
    private val builder: RestClient.Builder
) {
    private val log = LoggerFactory.getLogger(OpenWeatherClientConfig::class.java)

    @Bean
    fun openWeatherClient(builder: RestClient.Builder): OpenWeatherClient {
        val restClient = builder
            .baseUrl("https://api.openweathermap.org")
            .requestInterceptor { request, body, execution ->
                val principal = SecurityContextHolder.getContext().authentication?.name
                    ?: "anonymous"
                val response = execution.execute(request, body)
                log.info("[{}] [External API] {} {} [{}]", principal,
                    request.method, request.uri, response.statusCode)
                response
            }
            .build()
        val factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build()
        return factory.createClient(OpenWeatherClient::class.java)
    }
}