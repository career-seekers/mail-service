package org.careerseekers.csmailservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
class MailProperties {
    lateinit var host: String
    lateinit var port: String
    lateinit var protocol: String
    lateinit var debug: String
    var productionMail: ProductionMail = ProductionMail()
    var serviceMail: ServiceMail = ServiceMail()

    class ProductionMail {
        lateinit var username: String
        lateinit var password: String
    }

    class ServiceMail {
        lateinit var username: String
        lateinit var password: String
    }
}