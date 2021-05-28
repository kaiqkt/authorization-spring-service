package authorizationservice.application.controller

import authorizationservice.application.dto.toDomain
import authorizationservice.domain.services.PasswordService
import authorizationservice.domain.services.UserService
import authorizationservice.factories.UserRequestFactory
import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult

class UserControllerTest {
    private lateinit var userService: UserService
    private lateinit var passwordService: PasswordService
    private lateinit var userController: UserController
    private lateinit var result: BindingResult

    @BeforeEach
    fun beforeEach() {
        userService =  mockk(relaxed = true)
        passwordService =  mockk(relaxed = true)
        userController = UserController(userService, passwordService)
        result = mockk(relaxed = true)
    }

    @Test
    fun `given valid user request should return user and http status 201`() {
        val request = UserRequestFactory.sample()

        every { userService.create(request.toDomain()) } just runs

        val controller = userController.register(request, result)

        verify { userService.create(any()) }
        Assertions.assertEquals(HttpStatus.CREATED, controller.statusCode)
    }
}