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