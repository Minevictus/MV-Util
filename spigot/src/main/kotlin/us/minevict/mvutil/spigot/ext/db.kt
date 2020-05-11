package us.minevict.mvutil.spigot.ext

import co.aikar.idb.Database
import co.aikar.idb.DatabaseOptions
import co.aikar.idb.HikariPooledDatabase
import co.aikar.idb.PooledDatabaseOptions
import org.bukkit.plugin.Plugin

/**
 * [PooledDatabaseOptions] builder with the recommended options dialed in for a given [Plugin].
 *
 * @param username SQL user.
 * @param password SQL password.
 * @param database SQL database.
 * @param hostAndPort Host and port for the pool.
 * @return [PooledDatabaseOptions] built around this [Plugin].
 * @since 5.0.0
 */
fun Plugin.createRecommendedOptions(
    username: String,
    password: String,
    database: String,
    hostAndPort: String
): PooledDatabaseOptions = PooledDatabaseOptions.builder()
    .options(
        DatabaseOptions.builder()
            .poolName("${description.name} DB")
            .logger(logger)
            .mysql(username, password, database, hostAndPort)
            .build()
    )
    .build()

/**
 * Create an IDB [Database] instance to make SQL queries.
 *
 * @param username SQL user.
 * @param password SQL password.
 * @param database SQL database.
 * @param hostAndPort Host and port for the pool.
 * @return [Database] built around this [Plugin].
 * @since 5.0.0
 */
fun Plugin.createHikariDatabase(
    username: String,
    password: String,
    database: String,
    hostAndPort: String
): Database =
    HikariPooledDatabase(createRecommendedOptions(username, password, database, hostAndPort))