package authorizationservice.application.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/healthcheck")
class HealthCheckController(
    @Value("\${api.version}")
    private val version: String
) {

    @GetMapping
    fun check(): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("version" to version), HttpStatus.OK)
    }
}