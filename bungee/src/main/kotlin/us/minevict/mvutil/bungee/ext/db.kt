/**
 * MV-Util
 * Copyright (C) 2020 Mariell Hoversholm, Nahuel Dolores
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.minevict.mvutil.bungee.ext

import co.aikar.idb.Database
import co.aikar.idb.DatabaseOptions
import co.aikar.idb.HikariPooledDatabase
import co.aikar.idb.PooledDatabaseOptions
import net.md_5.bungee.api.plugin.Plugin

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