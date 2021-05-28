package authorizationservice.resources.config

import com.maxmind.geoip2.DatabaseReader
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils
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
    fun uaParser(): UserAgentAnalyzer {
        return UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000)
            .build()
    }
}
