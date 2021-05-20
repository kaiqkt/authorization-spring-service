package authorizationservice.doman.service

import authorizationservice.domain.exceptions.DataValidationException
import authorizationservice.domain.repositories.UserRepository
import authorizationservice.domain.services.UserService
import authorizationservice.factories.UserFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

private const val EMAIL_ERROR_MESSAGE = "The following email is already being used:"
private const val PERSON_ID_ERROR_MESSAGE = "The following personId is already being used:"
private const val PHONE_ERROR_MESSAGE = "The following phone is already being used:"

class UserServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @BeforeEach
    fun beforeEach() {
        userRepository = mockk(relaxed = true)
        bCryptPasswordEncoder = mockk(relaxed = true)
        userService = UserService(userRepository, bCryptPasswordEncoder)
    }

    @Test
    fun `given a valid user, should be create a user in database`() {
        val user = UserFactory.sample()

        every { userRepository.save(user) } returns user

        userService.create(user)

        verify { userRepository.save(any()) }
    }

    @Test
    fun `given a existing user email, should be return DataValidationException`() {
        val user = UserFactory.sample()
        val error = listOf("$EMAIL_ERROR_MESSAGE ${user.email}")

        every { userRepository.existsByEmail(user.email) } returns true

        val response = assertThrows<DataValidationException> {
            userService.create(user)
        }

        Assertions.assertEquals(error, response.details())
    }

    @Test
    fun `given a existing user person id, should be return DataValidationException`() {
        val user = UserFactory.sample()
        val error = listOf("$PERSON_ID_ERROR_MESSAGE ${user.personId}")

        every { userRepository.existsByPersonId(user.personId) } returns true

        val response = assertThrows<DataValidationException> {
            userService.create(user)
        }

        Assertions.assertEquals(error, response.details())
    }

    @Test
    fun `given a existing phone, should be return DataValidationException`() {
        val user = UserFactory.sample()
        val error = listOf("$PHONE_ERROR_MESSAGE ${user.phone}")

        every {
            userRepository.existsByPhone(user.phone)
        } returns true

        val response = assertThrows<DataValidationException> {
            userService.create(user)
        }

        Assertions.assertEquals(error, response.details())
    }
}