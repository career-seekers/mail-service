package org.careerseekers.csmailservice.config

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationInfo
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig {
    private val logger = LoggerFactory.getLogger(FlywayConfig::class.java)

    @Bean
    fun flyway(dataSource: DataSource): Flyway {
        logger.info("Flyway data source: {}", dataSource.javaClass.simpleName)

        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .load()

        flyway.migrate()

        logger.debug("=== Applied migrations ===")
        for (info: MigrationInfo in flyway.info().applied()) {
            logger.debug("{} - {}", info.version, info.description)
        }

        return flyway
    }
}