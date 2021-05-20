package authorizationservice.resources.security

import authorizationservice.domain.CONTENT_TYPE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val FAILURE_HANDLER =
    "{\n" + "\"status\": \"401\",\n" + " \"error\": \"Unauthorized\",\n" + "\"message\": \"Email or password wrong\"\n" + "}"

class AuthenticationFailureHandler : AuthenticationFailureHandler {
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