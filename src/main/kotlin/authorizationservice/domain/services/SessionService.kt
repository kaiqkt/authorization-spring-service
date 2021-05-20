package authorizationservice.domain.services

import authorizationservice.domain.CHANNEL_HEADER
import authorizationservice.domain.FORWARDED_HEADER
import authorizationservice.domain.USER_AGENT
import authorizationservice.domain.entities.*
import authorizationservice.domain.exceptions.SessionException
import authorizationservice.domain.repositories.SessionRepository
import com.google.common.base.Strings
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.exception.AddressNotFoundException
import com.maxmind.geoip2.exception.GeoIp2Exception
import com.maxmind.geoip2.model.CityResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import ua_parser.Client
import ua_parser.Parser
import java.io.IOException
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.servlet.http.HttpServletRequest

private const val UNKNOWN = "UNKNOWN"
private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
private val DATE_PARSE = LocalDateTime.now().format(FORMATTER)

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val userAgentParser: Parser,
    @Qualifier("GeoIPCity")
    private val dbReader: DatabaseReader
) {


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

            sessionRepository.save(session)
        } else {
            device.lastLogin = DATE_PARSE
            sessionRepository.save(device)
        }
    }

//    fun deleteSession(userId: String) = sessionRepository.deleteByUserId(userId)

    @Throws(IOException::class, GeoIp2Exception::class)
    private fun getLocation(ip: String): String {
        var location: String = UNKNOWN
        val ipAddress = InetAddress.getByName(ip)

        try {
            val cityResponse: CityResponse = dbReader.city(ipAddress)

            if (cityResponse.city != null && !cityResponse.city.name.isNullOrEmpty()) {
                location = cityResponse.city.name
            }
        } catch (ex: AddressNotFoundException) {
            location = UNKNOWN
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
        val client: Client = userAgentParser.parse(userAgent)

        return DeviceDetails(
            device = client.device.family,
            userAgent = "${client.userAgent.family} ${client.userAgent.major} ${client.userAgent.minor}",
            operationSystem = "${client.os.family} ${client.os.major} ${client.os.minor}"
        )
    }
// create notification for unknow device
//    private fun unknownDeviceNotification() {
//
//    }

}