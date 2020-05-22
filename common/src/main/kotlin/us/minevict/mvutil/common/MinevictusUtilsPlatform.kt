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
package us.minevict.mvutil.common

import co.aikar.commands.CommandManager
import co.aikar.idb.Database
import io.lettuce.core.RedisClient
import java.util.logging.Logger

/**
 * A generic Minevictus utils platform.
 *
 * @param CM Command manager, dependent on platform.
 * @param PL Plugin class, dependent on platform.
 * @since 0.1.0
 */
@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
interface MinevictusUtilsPlatform<PL : Any, CM : CommandManager<*, *, *, *, *, *>> {
    /**
     * Get the plugin's logger on the implemented platform.
     *
     * @return The plugin prefixed logger.
     * @since 0.1.0
     */
    val platformLogger: Logger

    /**
     * Get the global [RedisClient].
     *
     * @return The [RedisClient] shared by all plugins depending on this.
     * @since 3.7.0
     */
    val redis: RedisClient

    /**
     * Prepare the ACF command manager instance before it is used by plugins.
     *
     * @param commandManager The ACF command manager to use.
     * @return The same ACF command manager for chaining.
     */
    fun prepareAcf(commandManager: CM): CM

    /**
     * Prepare a [Database] for any given [PL].
     *
     * @param databaseName The name of the database you wish to use.
     * @param plugin Plugin that requests the [Database].
     * @return A new [Database] to use.
     * @since 4.0.0
     */
    fun prepareDatabase(databaseName: String, plugin: PL): Database
}