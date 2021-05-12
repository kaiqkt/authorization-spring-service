package authorizationservice.factories

import authorizationservice.application.dto.UserRequest

object UserRequestFactory {

    fun sample() = UserRequest(
        personId = "123456",
        email = "test@test.com",
        password = "@Test255",
        phone = PhoneRequestFactory.sample()
    )

}