package authorizationservice.doman.service

import authorizationservice.domain.CHANNEL_HEADER
import authorizationservice.domain.FORWARDED_HEADER
import authorizationservice.domain.USER_AGENT
import authorizationservice.domain.repositories.SessionRepository
import authorizationservice.domain.services.SessionService
import authorizationservice.factories.ClientFactory
import authorizationservice.factories.SessionFactory
import authorizationservice.factories.UserFactory
import com.maxmind.geoip2.DatabaseReader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ua_parser.Parser
import javax.servlet.http.HttpServletRequest

private const val USER_AGENT_RESPONSE = "Mozilla: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.3 Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/43.4"
private const val FORWARDED_RESPONSE = "8.211.173.158"
private const val CHANNEL_RESPONSE = "APP_MOBILE"

class SessionServiceTest {
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userAgentParser: Parser
    private lateinit var dbReader: DatabaseReader
    private lateinit var sessionService: SessionService
    private lateinit var request: HttpServletRequest

    @BeforeEach
    fun beforeEach() {
        sessionRepository = mockk(relaxed = true)
        userAgentParser = mockk(relaxed = true)
        dbReader = mockk(relaxed = true)
        request = mockk(relaxed = true)
        sessionService = SessionService(sessionRepository, userAgentParser, dbReader)
    }

    @Test
    fun `given a request a new session, should must be successfully created in database`() {
        val user = UserFactory.sample()
        val session = SessionFactory.sample()

        every { sessionRepository.findByUserId(user._id) } returns null
        every { userAgentParser.parse(USER_AGENT_RESPONSE) } returns ClientFactory.sample()
        every { request.getHeader(FORWARDED_HEADER) } returns FORWARDED_RESPONSE
        every { request.getHeader(USER_AGENT) } returns USER_AGENT_RESPONSE
        every { request.getHeader(CHANNEL_HEADER) } returns CHANNEL_RESPONSE
        every { sessionRepository.save(any()) } returns session

        sessionService.newSession(request, user)

        verify { sessionRepository.save(any()) }
    }

    @Test
    fun `given a session on an existing device should must update the last login`() {
        val user = UserFactory.sample()
        val session = SessionFactory.sample()

        every { sessionRepository.findByUserId(user._id) } returns listOf(session)
        every { userAgentParser.parse(USER_AGENT_RESPONSE) } returns ClientFactory.sample()
        every { request.getHeader(FORWARDED_HEADER) } returns FORWARDED_RESPONSE
        every { request.getHeader(USER_AGENT) } returns USER_AGENT_RESPONSE
        every { request.getHeader(CHANNEL_HEADER) } returns CHANNEL_RESPONSE
        every { sessionRepository.save(any()) } returns session

        sessionService.newSession(request, user)

        verify { sessionRepository.save(any()) }
    }
}