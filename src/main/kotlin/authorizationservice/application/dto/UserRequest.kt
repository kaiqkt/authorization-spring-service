package authorizationservice.application.dto

import authorizationservice.domain.entities.User
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserRequest(
    @get:NotEmpty(message = "Email cannot be empty.")
    val email: String = "",
    @get:NotEmpty(message = "Password cannot be empty.")
    @get:Pattern(regexp = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}\$", message = "Wrong password required")
    val password: String = ""
)

fun UserRequest.toDomain() = User(
    email = this.email,
    password = this.password
)

//^                         Start anchor
//(?=.*[A-Z].*[A-Z])        Ensure string has two uppercase letters.
//(?=.*[!@#$&*])            Ensure string has one special case letter.
//(?=.*[0-9].*[0-9])        Ensure string has two digits.
//(?=.*[a-z].*[a-z].*[a-z]) Ensure string has three lowercase letters.
//.{8}                      Ensure string is of length 8.
//$                         End anchor.