package authorizationservice.domain.repositories

import authorizationservice.domain.entities.Session
import org.springframework.data.mongodb.repository.MongoRepository

interface SessionRepository: MongoRepository<Session, String> {
    fun findByUserId(userId: String?): List<Session>?
}