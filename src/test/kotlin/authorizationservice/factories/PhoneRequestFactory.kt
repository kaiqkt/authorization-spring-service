package authorizationservice.factories

import authorizationservice.application.dto.PhoneRequest
import authorizationservice.application.dto.UserRequest

object PhoneRequestFactory {

    fun sample() = PhoneRequest(
        countryCode = PhoneFactory.sample().countryCode,
        areaCode = PhoneFactory.sample().areaCode,
        phoneNumber = PhoneFactory.sample().number
    )


}