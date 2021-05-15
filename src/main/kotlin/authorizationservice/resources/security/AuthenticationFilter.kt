package authorizationservice.resources.security

import authorizationservice.domain.entities.AuthSession
import authorizationservice.domain.entities.Channel
import authorizationservice.domain.entities.Login
import authorizationservice.domain.entities.User
import authorizationservice.domain.exceptions.LoginException
import authorizationservice.domain.repositories.RedisSessionRepository
import authorizationservice.domain.repositories.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val FAILURE_HANDLER = "{\n" + "\"status\": \"401\",\n" +" \"error\": \"Unauthorized\",\n" + "\"message\": \"Email or password wrong\"\n" + "}"

class AuthenticationFilter(
    jwtUtil: JWTUtil,
    authenticationManager: AuthenticationManager,
    userRepository: UserRepository,
    redisSessionRepository: RedisSessionRepository,
    expiration: String
) : UsernamePasswordAuthenticationFilter() {

    private val jwtUtil: JWTUtil
    private val userRepository: UserRepository
    private val redisRepository: RedisSessionRepository
    private val expiration: String

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        return try {
            val user = ObjectMapper().readValue(request.inputStream, Login::class.java)
            val authToken = UsernamePasswordAuthenticationToken(
                user.email,
                user.password,
                listOf<GrantedAuthority>(SimpleGrantedAuthority(ROLE_USER))
            )

            authenticationManager.authenticate(authToken)
        } catch (e: IOException) {
            throw LoginException("Invalid Authentication")
        }
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

        sessionDetails(request, token, user)

        response.contentType = CONTENT_TYPE
        response.addHeader(AUTHORIZATION_HEADER, "$BEARER_HEADER$token")
        response.addHeader(EXPOSE_AUTHORIZATION_HEADER, AUTHORIZATION_HEADER)
    }

    private fun sessionDetails(request: HttpServletRequest, token: String, user: User?) {
        val sessionUser = AuthSession(
            userId = user?._id,
            username = user?.email,
            personId = user?.personId,
            channel = Channel.valueOf(request.getHeader(CHANNEL_HEADER)),
            ip = request.getHeader(FORWARDED_HEADER) ?: request.remoteAddr,
            token = token,
            expiration = expiration.toLong(),
            timeUnit = TimeUnit.HOURS
        )
        redisRepository.createSession(sessionUser)
    }

    private inner class JWTAuthenticationFailureHandler : AuthenticationFailureHandler {
        @Throws(IOException::class, ServletException::class)
        override fun onAuthenticationFailure(
            request: HttpServletRequest,
            response: HttpServletResponse,
            exception: AuthenticationException
        ) {
            response.status = 401
            response.contentType = CONTENT_TYPE
            response.writer.append(FAILURE_HANDLER)
        }
    }

    init {
        setAuthenticationFailureHandler(JWTAuthenticationFailureHandler())
        this.jwtUtil = jwtUtil
        this.authenticationManager = authenticationManager
        this.userRepository = userRepository
        this.redisRepository = redisSessionRepository
        this.expiration = expiration
    }
}