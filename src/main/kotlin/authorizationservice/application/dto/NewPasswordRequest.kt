package authorizationservice.application.dto

import authorizationservice.domain.exceptions.PasswordException
import javax.validation.constraints.Pattern

data class NewPasswordRequest(
    @get:Pattern(regexp = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}\$", message = "Wrong password required")
    val password: String = "",
    @get:Pattern(regexp = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}\$", message = "Wrong password required")
    val samePassword: String = ""
){
    val isEquals
        get() = password == samePassword

}

fun NewPasswordRequest.toDomain(): String {
    if (this.isEquals) {
        return password
    } else {
        throw PasswordException("Invalid Password")
    }
}