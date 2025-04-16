package infrastructure.persistence
import com.typesafe.config.ConfigFactory

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        val config = ConfigFactory.load()
        val dsConfig = config.getConfig("config.datasource")

        val jdbcUri = dsConfig.getString("jdbcUri")
        val driver = dsConfig.getString("driver")
        val username = dsConfig.getString("username")
        val password = dsConfig.getString("password")

        Database.connect(
            url = jdbcUri,
            driver = driver,
            user = username,
            password = password
        )
    }
}
