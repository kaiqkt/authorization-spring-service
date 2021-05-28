package authorizationservice.application.controller

import authorizationservice.application.dto.NewPasswordRequest
import authorizationservice.application.dto.UserRequest
import authorizationservice.application.dto.toDomain
import authorizationservice.application.validation.RequestValidator
import authorizationservice.domain.services.PasswordService
import authorizationservice.domain.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val passwordService: PasswordService
    ) {

    @PostMapping
    fun register(@Valid @RequestBody user: UserRequest, result: BindingResult): ResponseEntity<HttpStatus> {
        RequestValidator.validate(result)
        userService.create(user.toDomain())

        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/reset/{email}")
    fun requestResetToken(@PathVariable email: String): ResponseEntity<HttpStatus> {
        passwordService.generateToken(email)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/change")
    fun changePassword(@PathParam("token")token: String, @RequestBody password: NewPasswordRequest): ResponseEntity<HttpStatus> {
        passwordService.changePassword(token, password.toDomain())

        return ResponseEntity(HttpStatus.CREATED)
    }
}