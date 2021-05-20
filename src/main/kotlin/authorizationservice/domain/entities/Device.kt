package authorizationservice.domain.entities

data class DeviceMetadata (
    val ip: String,
    val deviceDetails: DeviceDetails,
    val location: String,
    val channel: Channel,
)