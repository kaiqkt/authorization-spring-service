package authorizationservice.application.dto

import authorizationservice.domain.entities.Phone
import javax.validation.constraints.Pattern

data class PhoneRequest(
    @get:Pattern(regexp = "[0-9]{2}", message = "Country code invalid or empty")
    val countryCode: String = "",
    @get:Pattern(regexp = "[0-9]{2}", message = "Area code invalid or empty")
    val areaCode: String = "" ,
    @get:Pattern(regexp = "[0-9]{9}", message = "Number invalid or empty")
    val phoneNumber: String = ""
)

fun PhoneRequest.toDomain() = Phone(
    countryCode = this.countryCode,
    areaCode = this.areaCode,
    number = this.phoneNumber
)