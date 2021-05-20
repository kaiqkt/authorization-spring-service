package authorizationservice.resources.security.config

import com.maxmind.geoip2.DatabaseReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils
import ua_parser.Parser
import java.io.IOException

@Configuration
class DeviceAuthenticationConfig {
    @Bean(name = ["GeoIPCity"])
    @Throws(IOException::class)
    fun databaseReader(): DatabaseReader? {
        val database = ResourceUtils
            .getFile("target/classes/maxmind/GeoLite2-City.mmdb")
        return DatabaseReader.Builder(database)
            .build()
    }

    @Bean
    @Throws(IOException::class)
    fun uaParser(): Parser? {
        return Parser()
    }
}
