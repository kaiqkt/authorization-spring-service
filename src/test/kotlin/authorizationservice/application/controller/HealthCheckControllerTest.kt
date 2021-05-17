package authorizationservice.application.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HealthCheckControllerTest {
    private lateinit var version: String
    private lateinit var healthCheckController: HealthCheckController

    @BeforeEach
    fun beforeEach() {
        version = "2.0.0"
        healthCheckController = HealthCheckController(version)
    }

    @Test
    fun `given request should return api version and http status 200`() {

        val response = healthCheckController.check()

        Assertions.assertEquals("2.0.0", response.body?.get("version"))
    }
}