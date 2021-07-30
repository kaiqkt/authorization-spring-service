package authorizationservice.factories

import authorizationservice.application.dto.UserRequest

object UserRequestFactory {

    fun sample() = UserRequest(
        email = "test@test.com",
        password = "@Test255"
    )
}