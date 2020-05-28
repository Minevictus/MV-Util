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

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandManager
import co.aikar.idb.Database
import us.minevict.mvutil.common.acf.AcfCooldowns
import us.minevict.mvutil.common.channel.IPacketChannel
import us.minevict.mvutil.common.config.TomlConfiguration
import java.io.InputStream
import java.util.logging.Logger

/**
 * Shared interface by MvPlugin integrations on different platforms.
 *
 * @since 5.0.0
 */
@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
interface IMvPlugin<
        CM : CommandManager<*, *, *, *, *, *>,
        L : Any,
        PF : MinevictusUtilsPlatform<*, CM>
        > {
    /**
     * A [TomlConfiguration] specifically created for this plugin.
     *
     * @since 5.2.0
     */
    val tomlConfig: TomlConfiguration<*>

    /**
     * Called upon loading the plugin.
     *
     * Returns `true` if the plugin successfully loads, `false` otherwise.
     *
     * @return Whether the load was successful.
     * @throws Exception Any error encountered upon loading.
     */
    @Throws(Exception::class)
    fun load(): Boolean = true

    /**
     * Called upon enabling the plugin. This is not called if [load] erred in some way.
     *
     * Returns `true` if the plugin successfully enables, `false` otherwise.
     *
     * @return Whether the enable was successful.
     * @throws Exception Any error encountered upon enabling.
     */
    @Throws(Exception::class)
    fun enable(): Boolean = true

    /**
     * Called upon disabling the plugin. This is not called if [load] or [enable] erred in some way.
     *
     * @throws Exception Any error encountered upon disabling.
     */
    @Throws(Exception::class)
    fun disable() = Unit

    /**
     * Get the plugin's logger on the implemented platform.
     *
     * @return The plugin prefixed logger.
     */
    val platformLogger: Logger

    /**
     * Gets the [PF] instance.
     *
     * @return The [PF] implemented instance.
     */
    val mvUtil: PF

    /**
     * Gets a [CommandManager] linked to this plugin.
     *
     * This has already been prepared with [MinevictusUtilsPlatform.prepareAcf] and is only constructed once gotten.
     *
     * @return A newly constructed or cached [CommandManager] for this plugin.
     */
    val acf: CM

    /**
     * Gets a [Database] linked to this plugin.
     *
     * For the database name we will use the plugin name, replacing spaces for underscores and lower casing it.
     *
     * @return A newly constructed or cached [Database] for this plugin.
     */
    val database: Database

    /**
     * The name of the database the plugin will connect to if necessary.
     *
     * @return Database name.
     */
    var databaseName: String

    /**
     * Registers commands to this plugin's [CommandManager].
     *
     * This overwrites other commands in the same names by default.
     *
     * @param commands The commands to register.
     */
    fun registerCommands(vararg commands: BaseCommand)

    /**
     * Register listeners for this plugin.
     *
     * @param listeners The listeners to register.
     */
    fun listeners(vararg listeners: L)

    /**
     * Set up the tables for these cooldowns.
     *
     * @param database  The database to set up the tables within.
     * @param cooldowns The cooldowns to setup tables for.
     * @param C The type of the cooldown.
     */
    fun <C : AcfCooldowns> setupCooldowns(database: Database, cooldowns: Array<C>) {
        for (cooldown in cooldowns) {
            cooldown.setupTable(database).exceptionally {
                platformLogger.warning("Exception when setting up cooldown")
                it.printStackTrace()
            }
        }
    }

    /**
     * Register a packet channel and handle its unregistering.
     *
     * @param channel The channel to register and handle.
     * @param P The type of packet for the channel to handle.
     * @param C The type of the channel.
     * @return The channel given.
     * @since 3.8.0
     */
    fun <P, C : IPacketChannel<P, *>?> packetChannel(channel: C): C

    /**
     * Get the plugin's name.
     *
     * @return The plugin's name.
     */
    val pluginName: String

    /**
     * Get the plugin's current version.
     *
     * @return The current version of the plugin.
     */
    val version: String

    /**
     * Get a resource from a plugin jar as an [InputStream] if it exists.
     *
     * @param resourceName The name of the resource in the jar. This is relative to the root of the jar.
     * @return The resource if found, or `null` otherwise.
     * @since 5.2.1
     */
    fun getPluginResourceAsInputStream(resourceName: String): InputStream?

    enum class PluginErrorState {
        LOAD,
        ENABLE,
    }
}