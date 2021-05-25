package authorizationservice.domain.entities

import com.fasterxml.jackson.annotation.JsonIgnore

data class DeviceMetadata (
    @JsonIgnore
    val ip: String,
    val deviceDetails: DeviceDetails,
    val location: String,
    val channel: Channel,
)