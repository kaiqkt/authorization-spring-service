package authorizationservice.resources.security

import authorizationservice.domain.*
import authorizationservice.domain.entities.Login
import authorizationservice.domain.repositories.UserRepository
import authorizationservice.domain.services.SessionService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    jwtUtil: JWTUtil,
    authenticationManager: AuthenticationManager,
    userRepository: UserRepository,
    sessionService: SessionService
) : UsernamePasswordAuthenticationFilter() {

    private val jwtUtil: JWTUtil
    private val userRepository: UserRepository
    private val sessionService: SessionService

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val user = ObjectMapper().readValue(request.inputStream, Login::class.java)
        val authToken = UsernamePasswordAuthenticationToken(
            user.email,
            user.password,
            listOf<GrantedAuthority>(SimpleGrantedAuthority(ROLE_USER))
        )

        return authenticationManager.authenticate(authToken)

    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val username: String = (authResult.principal as UserDetailsImpl).username

        val user = userRepository.findByEmail(username)
        val token = jwtUtil.generateToken(user?.personId)

        sessionService.newSession(request, user)

        logger.info("Successfully authenticated [user: ${user?._id}]")

        response.contentType = CONTENT_TYPE
        response.addHeader(AUTHORIZATION_HEADER, "$BEARER_HEADER$token")
        response.addHeader(EXPOSE_AUTHORIZATION_HEADER, AUTHORIZATION_HEADER)
    }

    init {
        setAuthenticationFailureHandler(AuthenticationFailureHandler())
        this.jwtUtil = jwtUtil
        this.authenticationManager = authenticationManager
        this.userRepository = userRepository
        this.sessionService = sessionService
    }
}