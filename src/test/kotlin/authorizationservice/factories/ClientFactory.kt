package authorizationservice.factories

import ua_parser.Client
import ua_parser.Device
import ua_parser.OS
import ua_parser.UserAgent

object ClientFactory {
    fun sample() = Client(
        UserAgent("Mozilla Mobile", "5", "1", ""),
        OS("IOS", "5", "1", "", ""),
        Device("Iphone")
    )
}