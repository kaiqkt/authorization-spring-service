package authorizationservice.domain.repositories

import authorizationservice.domain.entities.ResetPassword
import authorizationservice.domain.exceptions.SessionException
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class PasswordRepository(
    private val redisTemplate: RedisTemplate<String, ResetPassword>
) {

    private var hashOperations: HashOperations<String, String, ResetPassword> = redisTemplate.opsForHash()
    private var key = "reset-password"
    private var expiration: Long = 10

    private val logger = LoggerFactory.getLogger(javaClass)

    fun save(reset: ResetPassword) {
        try {
            hashOperations.put(key, reset.token, reset)
            redisTemplate.expire(key, expiration, TimeUnit.MINUTES)
            logger.info("Saved new reset password for [user: ${reset.userId}]")

        } catch (ex: Exception) {
            throw SessionException(ex.message)
        }
    }

    fun find(token: String): ResetPassword? {
        try {
            logger.info("Attempt to find reset password by [token: $token]")
            return hashOperations.get(key, token)
        } catch (ex: Exception) {
            throw SessionException(ex.message)
        }
    }

    fun delete(token: String) {
        try {
            hashOperations.delete(key, token)
            logger.info("Deleted reset password by [token: $token]")
        } catch (ex: Exception) {
            throw SessionException(ex.message)
        }
    }
}