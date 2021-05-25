package authorizationservice.application.controller

import authorizationservice.domain.entities.Session
import authorizationservice.domain.services.SessionService
import authorizationservice.resources.security.JWTUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sessions")
class SessionController(
    private val sessionService: SessionService,
    private val jwtUtil: JWTUtil
) {

    @DeleteMapping
    fun logout(@RequestHeader("SessionId") sessionId: String): ResponseEntity<HttpStatus> {
        sessionService.deleteSession(sessionId)

        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping
    fun sessions(@RequestHeader("Authorization") token: String): ResponseEntity<List<Session>?> {
        return ResponseEntity<List<Session>?>(
            sessionService.findSessions(jwtUtil.getUserId(token.substring(7))),
            HttpStatus.OK
        )
    }
}