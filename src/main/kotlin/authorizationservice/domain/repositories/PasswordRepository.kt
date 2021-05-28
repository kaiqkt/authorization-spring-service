package authorizationservice.domain.repositories

import authorizationservice.domain.entities.ResetPassword
import org.springframework.data.mongodb.repository.MongoRepository

interface PasswordRepository: MongoRepository<ResetPassword, String> {
    fun findByToken(token: String): ResetPassword
    fun deleteAllByUserId(userId: String)
}