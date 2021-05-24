package authorizationservice.factories

import authorizationservice.domain.entities.Channel
import authorizationservice.domain.entities.DeviceDetails
import authorizationservice.domain.entities.DeviceMetadata
import authorizationservice.domain.entities.Session
import io.azam.ulidj.ULID
import java.time.LocalDateTime

object SessionFactory {
    fun sample() = Session(
        userId = ULID.random(),
        deviceMetadata = DeviceMetadata(
            ip = "8.211.173.158",
            deviceDetails = DeviceDetails(),
            location = "UNKNOWN",
            channel = Channel.APP_MOBILE
        ),
        lastLogin = LocalDateTime.now().toString()
    )
}