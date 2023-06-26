package sdcc.accounting.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import sdcc.accounting.services.TokenService

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val tokenService: TokenService,
    private val jwtConfig: JwtConfig
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorizeHttpRequests ->
            authorizeHttpRequests.anyRequest().permitAll()
        }
        http.oauth2ResourceServer { oauth2ResourceServer ->
            oauth2ResourceServer.jwt { jwt ->
                jwt.decoder(jwtConfig.jwtDecoder())
            }
        }
        http.authenticationManager { authenticationManager ->
            val jwt = authenticationManager as BearerTokenAuthenticationToken
            val user = tokenService.parseToken(jwt.token)
            UsernamePasswordAuthenticationToken(
                user,
                "",
                listOf(SimpleGrantedAuthority("USER"))
            )
        }
        http.cors { corsConfigurationSource() }
        http.sessionManagement { sessionManager ->
            sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.csrf { csrf ->
            csrf.disable()
        }
        http.headers { headers ->
            headers.frameOptions { frameOptions ->
                frameOptions.disable()
            }
            headers.xssProtection { xssProtection ->
                xssProtection.disable()
            }
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}