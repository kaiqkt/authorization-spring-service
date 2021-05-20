package authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootApplication
class AuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServiceApplication>(*args)
}
