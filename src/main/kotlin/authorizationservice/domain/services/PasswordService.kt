package authorizationservice.domain.services

import authorizationservice.domain.entities.ResetPassword
import authorizationservice.domain.repositories.PasswordRepository
import authorizationservice.domain.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class PasswordService(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val passwordRepository: PasswordRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun generateToken(email: String) {
        userRepository.findByEmail(email)?.let {
            val resetPassword = ResetPassword(
                token = UUID.randomUUID().toString().replace("-", ""),
                userId = it._id
            )
            logger.info("Generated token for reset password to [user: ${it._id}]")

            passwordRepository.save(resetPassword)

            //enviar para o service de email
        }

    }

    fun changePassword(token: String, password: String) {
        passwordRepository.find(token)?.let {
            userRepository.findById(it.userId).get().let { user ->
                user.password = bCryptPasswordEncoder.encode(password)

                userRepository.save(user)
                passwordRepository.delete(token)
            }
        }
    }
}