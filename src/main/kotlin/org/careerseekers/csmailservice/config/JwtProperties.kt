package org.careerseekers.csmailservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "config.jwt")
class JwtProperties {
    lateinit var secret: String
}