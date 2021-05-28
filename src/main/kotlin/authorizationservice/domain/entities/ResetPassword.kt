package authorizationservice.domain.entities

import io.azam.ulidj.ULID
import org.springframework.data.annotation.Id
import java.io.Serializable


data class ResetPassword(
    @Id
    val _id: String = ULID.random(),
    val token: String,
    val userId: String
): Serializable