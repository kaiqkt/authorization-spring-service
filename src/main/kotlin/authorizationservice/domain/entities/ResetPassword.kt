package authorizationservice.domain.entities

import io.azam.ulidj.ULID
import org.springframework.data.annotation.Id
import java.util.*


data class ResetPassword(
    @Id
    val _id: String = ULID.random(),
    val token: String,
    val userId: String,
    val expireTime: Date
) {

    val isNotExpired
        get() = Date(System.currentTimeMillis()).before(expireTime)
}