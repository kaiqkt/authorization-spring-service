package authorizationservice.domain.services

import authorizationservice.domain.CHANNEL_HEADER
import authorizationservice.domain.FORWARDED_HEADER
import authorizationservice.domain.USER_AGENT
import authorizationservice.domain.entities.*
import authorizationservice.domain.repositories.SessionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.exception.AddressNotFoundException
import com.maxmind.geoip2.exception.GeoIp2Exception
import com.maxmind.geoip2.model.CityResponse
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest

private const val UNKNOWN = "UNKNOWN"
private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
private val DATE_PARSE = LocalDateTime.now().format(FORMATTER)

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val uaParser: UserAgentAnalyzer,
    @Qualifier("GeoIPCity")
    private val dbReader: DatabaseReader
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun newSession(request: HttpServletRequest, user: User?) {
        val ip = request.getHeader(FORWARDED_HEADER) ?: request.remoteAddr
        val location = getLocation(ip)
        val deviceDetails = getDeviceDetails(request.getHeader(USER_AGENT))
        val channel = Channel.valueOf(request.getHeader(CHANNEL_HEADER))

        val device = findExistingDevice(user?._id, deviceDetails, location)

        if (device == null) {
            val newDevice = DeviceMetadata(
                ip = ip,
                deviceDetails = deviceDetails,
                location = location,
                channel = channel
            )

            val session = Session(
                userId = user?._id,
                deviceMetadata = newDevice,
                lastLogin = DATE_PARSE
            )

            sessionRepository.save(session).also {

                logger.info("New session created for user: ${it.userId}")
            }
        } else {
            device.lastLogin = DATE_PARSE
            sessionRepository.save(device)

            logger.info("Updated last login for session: ${device._id}]")
        }
    }

//    fun deleteSession(userId: String) = sessionRepository.deleteByUserId(userId)

    @Throws(IOException::class, GeoIp2Exception::class)
    private fun getLocation(ip: String): String {
        var location: String = UNKNOWN
        val ipAddress = InetAddress.getByName(ip)

        logger.info("Attempt to get location")

        try {
            val cityResponse: CityResponse = dbReader.city(ipAddress)

            if (cityResponse.city != null && !cityResponse.city.name.isNullOrEmpty()) {
                location = cityResponse.city.name
                logger.info("Location get with success")
            }
        } catch (ex: AddressNotFoundException) {
            location = UNKNOWN
            logger.info("Failed to get location")
        }

        return location
    }

    private fun findExistingDevice(userId: String?, deviceDetails: DeviceDetails, location: String): Session? {
        sessionRepository.findByUserId(userId)?.let { it ->
            it.map { session ->
                if (session.deviceMetadata.deviceDetails == deviceDetails && session.deviceMetadata.location == location) {
                    return session
                }
            }
        }
        return null
    }

    private fun getDeviceDetails(userAgent: String): DeviceDetails {
        val ua = uaParser.parse(userAgent)

        return ObjectMapper().readValue(ua.toJson(), DeviceDetails::class.java)
    }
// create notification for unknow device
//    private fun unknownDeviceNotification() {
//
//    }

}