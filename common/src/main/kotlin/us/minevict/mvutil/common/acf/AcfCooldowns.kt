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
package us.minevict.mvutil.common.acf

import co.aikar.commands.CommandIssuer
import co.aikar.idb.Database
import us.minevict.mvutil.common.utils.Platformless
import java.sql.SQLException
import java.util.concurrent.CompletableFuture

/**
 * Marker for which cooldowns are present in ACF.
 *
 * @since 5.0.0
 */
@Suppress("SqlNoDataSourceInspection")
interface AcfCooldowns {
    /**
     * Get the duration in milliseconds specific to this issuer and this cooldown.
     *
     * @param issuer The issuer to get the duration of.
     * @return The duration for this issuer in milliseconds.
     */
    fun getDuration(issuer: CommandIssuer): Long

    /**
     * The name of this cooldown. It is safe to just return [Enum.name] here.
     *
     * @return The name of this cooldown.
     */
    fun getCooldownName(): String {
        if (this is Enum<*>) {
            return name
        }
        return javaClass.simpleName
    }

    /**
     * Set up the table for this cooldown.
     *
     * There must be a column called `player` with the type `char(36)`. There must be a column
     * called `lastExecuted` with the type `long`.
     *
     * @param database The database to use for the data.
     * @return A future which completes once the table is set up.
     */
    fun setupTable(database: Database): CompletableFuture<Unit> {
        return Platformless.runAsync {
            val tableName = getCooldownTableName()
            try {
                database.executeUpdate(
                    """
            CREATE TABLE IF NOT EXISTS $tableName (
                player CHAR(36) NOT NULL,
                lastExecuted BIGINT NOT NULL DEFAULT 0,
                PRIMARY KEY (player)
            )
            """.trimIndent()
                )
            } catch (ex: SQLException) {
                throw RuntimeException("error setting up cooldowns table $tableName", ex)
            }
            Unit
        }
    }

    /**
     * Get the table name for this cooldown.
     *
     * @return The table name for this cooldown.
     */
    fun getCooldownTableName(): String {
        return "cooldowns_${getCooldownName()}"
    }
}