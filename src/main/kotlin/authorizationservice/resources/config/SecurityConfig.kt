package authorizationservice.resources.config

import authorizationservice.domain.repositories.UserRepository
import authorizationservice.domain.services.SessionService
import authorizationservice.resources.security.AuthenticationFilter
import authorizationservice.resources.security.AuthorizationFilter
import authorizationservice.resources.security.JWTUtil
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
    private val sessionService: SessionService,
    @Value("\${token}")
    private var secret: String
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, *POST_MATCHERS).hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, *GET_MATCHERS).hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, *DELETE_MATCHERS).hasRole("USER")
            .anyRequest().authenticated()
        http.addFilter(
            AuthenticationFilter(
                jwtUtil,
                authenticationManager(),
                userRepository,
                sessionService
            )
        )
        http.addFilter(AuthorizationFilter(authenticationManager(), userRepository, jwtUtil, userDetailsService, secret, sessionService))
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
        private val POST_MATCHERS = arrayOf(
            "/users"
        )

        private val DELETE_MATCHERS = arrayOf(
            "/users"
        )

        private val GET_MATCHERS = arrayOf(
            "/healtcheck"
        )
    }
}
