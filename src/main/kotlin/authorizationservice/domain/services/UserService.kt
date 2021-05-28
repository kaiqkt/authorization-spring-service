package authorizationservice.domain.services

import authorizationservice.domain.entities.User
import authorizationservice.domain.exceptions.DataValidationException
import authorizationservice.domain.repositories.UserRepository
import authorizationservice.resources.security.UserDetailsImpl
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private const val EMAIL_ERROR_MESSAGE = "The following email is already being used:"
private const val PERSON_ID_ERROR_MESSAGE = "The following personId is already being used:"
private const val PHONE_ERROR_MESSAGE = "The following phone is already being used:"

@Service
class UserService(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(user: User) {
        validateDate(user)

        user.password = bCryptPasswordEncoder.encode(user.password)
        user.createdAt = LocalDateTime.now()
        val newUser = userRepository.save(user)

        logger.info("User[${newUser._id}] created in the mongo database")
    }

    private fun validateDate(user: User) {
        val error = mutableListOf<String>()

        when {
            userRepository.existsByEmail(user.email) -> error.add("$EMAIL_ERROR_MESSAGE ${user.email}")
            userRepository.existsByPhone(user.phone) -> error.add("$PHONE_ERROR_MESSAGE ${user.phone}")
        }

        if (error.isNotEmpty()) throw DataValidationException(error)
    }
}