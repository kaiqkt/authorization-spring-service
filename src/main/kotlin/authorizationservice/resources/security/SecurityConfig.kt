package authorizationservice.resources.security

import authorizationservice.domain.repositories.RedisSessionRepository
import authorizationservice.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JWTUtil,
    private val userRepository: UserRepository,
    private val redisSessionRepository: RedisSessionRepository,
    @Value("\${token}")
    private var secret: String,
    @Value("\${redis.expiration}")
    private var expiration: String
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, *MATCHERS).hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, *MATCHERS).hasRole("USER")
            .anyRequest().authenticated()
        http.addFilter(
            AuthenticationFilter(
                jwtUtil,
                authenticationManager(),
                userRepository,
                redisSessionRepository,
                expiration
            )
        )
        http.addFilter(AuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService, secret))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val cors = CorsConfiguration().applyPermitDefaultValues()
        cors.allowedMethods = listOf("POST", "GET", "PUT", "DELETE", "OPTIONS")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", cors)
        return source
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    companion object {
        private val MATCHERS = arrayOf(
            "/users"
        )
    }
}
