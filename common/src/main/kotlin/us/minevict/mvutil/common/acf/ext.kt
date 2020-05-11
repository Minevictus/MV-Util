package us.minevict.mvutil.common.acf

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.CommandManager
import co.aikar.idb.Database
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import us.minevict.mvutil.common.ext.duration
import us.minevict.mvutil.common.ext.red
import us.minevict.mvutil.common.ext.yellow
import us.minevict.mvutil.common.utils.Platformless
import java.sql.SQLException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The default cooldown error message.
 *
 * @since 5.0.0
 */
val DEFAULT_COOLDOWN_ERROR_MESSAGE: (CommandIssuer, Long) -> Array<BaseComponent> = { _, expiry ->
    val left = expiry - System.currentTimeMillis()
    ComponentBuilder("This command is on cooldown for another ")
        .red()
        .duration(left, TimeUnit.MILLISECONDS)
        .yellow()
        .red(".")
        .create()
}

/**
 * Enables the help feature in the given [CommandManager].
 *
 * @param commandManager The [CommandManager] to enable help for.
 * @since 5.0.0
 */
fun CommandManager<*, *, *, *, *, *>.enableHelpFeature(): Unit =
    @Suppress("DEPRECATION") // Developer warning.
    enableUnstableAPI("help")

/**
 * Check whether the player executing this is on a cooldown.
 *
 * @param duration The duration this command has a cooldown for in milliseconds. Use [TimeUnit] to have
 * easy calculation of this.
 * @param fetchLastExecuted Function to fetch last time the player executed the command or null if never.
 * @param setLastExecuted Consumer to set the last time the player executed this command.
 * @param resetIfNotExpired Whether to always set the last time the player executed the command.
 * @param errorMessage The error message to send players who are on cooldowns.
 * @param Issuer The type of issuer for this command invocation.
 * @return Whether the player is currently on a cooldown.
 * @since 5.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <Issuer : CommandIssuer> BaseCommand.cooldown(
    duration: Long,
    fetchLastExecuted: (Issuer) -> Long?,
    setLastExecuted: (Issuer, Long) -> Unit,
    resetIfNotExpired: Boolean = false,
    errorMessage: ((Issuer, Long) -> Array<BaseComponent>?)? = DEFAULT_COOLDOWN_ERROR_MESSAGE
): Boolean {
    val issuer = currentCommandIssuer as Issuer
    if (!issuer.isPlayer) {
        return false
    }

    val lastExecuted = fetchLastExecuted(issuer)
    if (lastExecuted == null) {
        setLastExecuted(issuer, System.currentTimeMillis())
        return false
    }
    val now = System.currentTimeMillis()
    val expiry = lastExecuted + duration

    if (expiry <= now || resetIfNotExpired) {
        setLastExecuted(issuer, now)
    }
    if (expiry <= now) {
        return false
    }

    errorMessage?.invoke(issuer, expiry)?.let {
        Platformless.sendMessage(issuer, it)
    }

    return true
}

/**
 * Check whether the player executing this is on a cooldown.
 *
 * This does not verify threading and **will block the main thread** if not executed elsewhere.
 *
 * @param cooldownData The [AcfCooldowns] instance for this cooldown. The table must have been set up already.
 * @param database The [Database] to use for this cooldown check.
 * @param resetIfNotExpired Whether to always set the last time the player executed the command.
 * @param errorMessage The error message to send players who are on cooldowns.
 * @param Issuer The type of issuer for this command invocation.
 * @return Whether the player is currently on a cooldown.
 * @since 5.0.0
 */
@Suppress("SqlNoDataSourceInspection")
fun <Issuer : CommandIssuer> BaseCommand.cooldown(
    cooldownData: AcfCooldowns,
    database: Database,
    resetIfNotExpired: Boolean = false,
    errorMessage: ((Issuer, Long) -> Array<BaseComponent>?)? = DEFAULT_COOLDOWN_ERROR_MESSAGE
) = cooldown(
    cooldownData.getDuration(currentCommandIssuer),
    {
        try {
            database.getFirstRow(
                """
          SELECT lastExecuted FROM ${cooldownData.getCooldownTableName()} WHERE player = ?
        """.trimIndent(),
                it.uniqueId.toString()
            )?.getLong("lastExecuted")
        } catch (ex: SQLException) {
            throw RuntimeException(
                "exception while fetching last executed for cooldown ${cooldownData.getCooldownName()}",
                ex
            )
        }
    },
    { it, newValue ->
        try {
            database.executeUpdate(
                """
          INSERT INTO ${cooldownData.getCooldownTableName()} (player, lastExecuted) VALUES (?, ?) ON DUPLICATE KEY UPDATE lastExecuted = ?
        """.trimIndent(),
                it.uniqueId.toString(),
                newValue,
                newValue
            )
        } catch (ex: SQLException) {
            throw RuntimeException(
                "exception while setting last executed for cooldown ${cooldownData.getCooldownName()}",
                ex
            )
        }
    },
    resetIfNotExpired,
    errorMessage
)

/**
 * Check whether the player executing this is on a cooldown.
 *
 * @param duration The duration this command has a cooldown for in milliseconds. Use [TimeUnit] to have
 * easy calculation of this.
 * @param map The map to store cooldown data in.
 * @param resetIfNotExpired Whether to always set the last time the player executed the command.
 * @param errorMessage The error message to send players who are on cooldowns.
 * @param Issuer The type of issuer for this command invocation.
 * @return Whether the player is currently on a cooldown.
 * @since 5.0.0
 */
fun <Issuer : CommandIssuer> BaseCommand.cooldown(
    duration: Long,
    map: MutableMap<UUID, Long>,
    resetIfNotExpired: Boolean = false,
    errorMessage: ((Issuer, Long) -> Array<BaseComponent>?)? = DEFAULT_COOLDOWN_ERROR_MESSAGE
): Boolean = cooldown(
    duration,
    { map[it.uniqueId] },
    { it, new -> map[it.uniqueId] = new },
    resetIfNotExpired,
    errorMessage
)
