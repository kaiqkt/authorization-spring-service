package authorizationservice.application.controller

import authorizationservice.domain.services.SessionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sessions")
class SessionController(private val sessionService: SessionService) {

    @DeleteMapping
    fun logout(@RequestHeader("SessionId") sessionId: String): ResponseEntity<HttpStatus> {
        sessionService.deleteSession(sessionId)

        return ResponseEntity(HttpStatus.OK)
    }
}