package authorizationservice.application.controller

import authorizationservice.application.dto.UserRequest
import authorizationservice.application.dto.toDomain
import authorizationservice.application.validation.RequestValidator
import authorizationservice.domain.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun register(@Valid @RequestBody user: UserRequest, result: BindingResult): ResponseEntity<HttpStatus> {
        RequestValidator.validate(result)
        userService.create(user.toDomain())

        return ResponseEntity(HttpStatus.CREATED)
    }

    //update password
}