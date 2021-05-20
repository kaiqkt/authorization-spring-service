package authorizationservice.domain.entities

import io.azam.ulidj.ULID
import org.springframework.data.annotation.Id
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

data class Session(
    @Id
    val _id: String = ULID.random(),
    val userId: String?,
    val deviceMetadata: DeviceMetadata,
    var lastLogin: String
)
