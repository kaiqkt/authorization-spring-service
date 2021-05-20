package authorizationservice.domain.entities

data class DeviceDetails (
    val device: String,
    val userAgent: String,
    val operationSystem: String
)