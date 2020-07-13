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
package us.minevict.mvutil.spigot

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import com.google.common.reflect.TypeToken
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import us.minevict.mvutil.common.IMvPlugin
import us.minevict.mvutil.common.channel.IPacketChannel
import us.minevict.mvutil.common.config.TomlConfiguration
import us.minevict.mvutil.common.config.TomlDelegatedConfigurationValue
import us.minevict.mvutil.common.ext.typeToken
import us.minevict.mvutil.spigot.permissions.PermissionsDSL
import java.io.File
import java.io.InputStream
import java.util.logging.Logger
import kotlin.reflect.KClass

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 5.0.0
 */
abstract class MvPlugin : JavaPlugin(),
    IMvPlugin<PaperCommandManager, Listener, MinevictusUtilsSpigot> {
    override val platformLogger: Logger
        get() = logger

    private val tomlConfigDelegate: TomlConfiguration<MvPlugin> by lazy {
        TomlConfiguration(this, File(dataFolder, "config.toml"), "config.toml")
    }
    override val tomlConfig: TomlConfiguration<*>
        get() = tomlConfigDelegate

    private val acfDelegate = lazy {
        mvUtil.prepareAcf(PaperCommandManager(this))
    }
    override val acf by acfDelegate

    private val databaseDelegate = lazy {
        mvUtil.prepareDatabase(databaseName, this)
    }
    override val database by databaseDelegate

    override val mvUtil: MinevictusUtilsSpigot
        get() = MinevictusUtilsSpigot.instance

    override lateinit var databaseName: String

    private val tasks = mutableListOf<BukkitTask>()
    private val channels = mutableListOf<IPacketChannel<*, *>>()
    private var errorState: IMvPlugin.PluginErrorState? = null

    override val pluginName: String
        get() = description.name

    override val version: String
        get() = description.version

    final override fun onLoad() {
        databaseName = pluginName.toLowerCase().replace('-', '_')

        try {
            if (!load()) {
                errorState = IMvPlugin.PluginErrorState.LOAD
                logger.warning("Disabling plugin...")
                isEnabled = false
            }
        } catch (ex: Exception) {
            errorState = IMvPlugin.PluginErrorState.LOAD
            logger.severe("Encountered exception upon loading:")
            ex.printStackTrace()
            logger.warning("Disabling plugin...")
            isEnabled = false
        }

        if (errorState != null) {
            shutdownSafely()
        }
    }

    final override fun onEnable() {
        if (errorState != null) return

        try {
            if (!enable()) {
                errorState = IMvPlugin.PluginErrorState.ENABLE
                logger.warning("Disabling plugin...")
                isEnabled = false
            }
        } catch (ex: Exception) {
            errorState = IMvPlugin.PluginErrorState.ENABLE
            logger.severe("Encountered exception upon enabling:")
            ex.printStackTrace()
            logger.warning("Disabling plugin...")
            isEnabled = false
        }

        if (!isEnabled) {
            shutdownSafely()
        }
    }

    final override fun onDisable() {
        if (errorState != null) return

        try {
            disable()
        } catch (ex: Exception) {
            logger.severe("Encountered exception upon disabling:")
            ex.printStackTrace()
        }

        shutdownSafely()
    }

    private fun shutdownSafely() {
        tasks.forEach(BukkitTask::cancel)
        HandlerList.unregisterAll(this)
        if (acfDelegate.isInitialized())
            acf.unregisterCommands()
        channels.forEach(IPacketChannel<*, *>::unregisterIncoming)
        if (databaseDelegate.isInitialized())
            database.close()
    }

    override fun listeners(vararg listeners: Listener) {
        listeners.forEach {
            server.pluginManager.registerEvents(it, this)
        }
    }

    override fun <P, C : IPacketChannel<P, *>?> packetChannel(channel: C): C {
        channels.add(channel as IPacketChannel<*, *>)
        return channel
    }

    fun tasks(vararg tasks: BukkitTask) {
        this.tasks.addAll(tasks)
    }

    override fun registerCommands(vararg commands: BaseCommand) {
        registerCommands(true, *commands)
    }

    fun registerCommands(force: Boolean, vararg commands: BaseCommand) {
        commands.forEach {
            acf.registerCommand(it, force)
        }
    }

    fun permissions(block: PermissionsDSL.() -> Unit) {
        PermissionsDSL().also(block)
    }

    override fun getPluginResourceAsInputStream(resourceName: String): InputStream? = getResource(resourceName)

    /**
     * Delegate the config value requested with type [R] to the name or the property name with an optional default.
     *
     * @return The config value delegate.
     * @since 5.2.0
     */
    @Suppress("DEPRECATION")
    inline fun <reified R : Any> config(
        name: String? = null,
        noinline default: () -> R? = { null }
    ) = __config_internal_getter(name, typeToken<R>(), R::class, default)

    /**
     * @since 5.2.0
     */
    @Suppress("UnstableApiUsage", "DeprecatedCallableAddReplaceWith")
    @Deprecated("This is an internal API.")
    private fun <R : Any> config(
        name: String?,
        typeToken: TypeToken<out R>,
        type: KClass<out R>,
        default: () -> R?
    ) = TomlDelegatedConfigurationValue(name, typeToken, type, default)

    /**
     * @since 5.2.0
     */
    @Suppress("FunctionName", "DEPRECATION", "DeprecatedCallableAddReplaceWith", "UnstableApiUsage")
    @PublishedApi
    @Deprecated("This is an internal API.")
    internal fun <R : Any> __config_internal_getter(
        name: String?,
        typeToken: TypeToken<out R>,
        type: KClass<out R>,
        default: () -> R?
    ) = config(name, typeToken, type, default)
}