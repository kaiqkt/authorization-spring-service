package authorizationservice.domain.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import io.azam.ulidj.ULID
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class User(
    @Id
    val _id: String = ULID.random(),
    val email: String,
    @JsonIgnore
    var password: String,
    var createdAt: LocalDateTime? = null
)