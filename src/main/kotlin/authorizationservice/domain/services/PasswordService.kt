package authorizationservice.domain.services

import authorizationservice.domain.entities.ResetPassword
import authorizationservice.domain.exceptions.ExpiredResetPasswordException
import authorizationservice.domain.repositories.PasswordRepository
import authorizationservice.domain.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class PasswordService(
    private val passwordRepository: PasswordRepository,
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    private val expirationTime: Long = 600000

    fun generateToken(email: String) {
        userRepository.findByEmail(email)?.let {
            val resetPassword = ResetPassword(
                token = UUID.randomUUID().toString().replace("-", ""),
                userId = it._id,
                expireTime = Date(System.currentTimeMillis() + expirationTime)
            )

            passwordRepository.save(resetPassword)

            //enviar para o service de email
        }

    }

    fun changePassword(token: String, password: String) {
        val resetPassword = passwordRepository.findByToken(token)

        if (!resetPassword.isNotExpired){
            throw ExpiredResetPasswordException("Reset token expired")
        }

        userRepository.findById(resetPassword.userId).get().let {
            it.password = bCryptPasswordEncoder.encode(password)

            userRepository.save(it)
            passwordRepository.deleteAllByUserId(it._id)
        }
    }
}