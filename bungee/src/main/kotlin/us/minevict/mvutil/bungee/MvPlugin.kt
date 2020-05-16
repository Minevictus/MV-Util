package us.minevict.mvutil.bungee

import co.aikar.commands.BaseCommand
import co.aikar.commands.BungeeCommandManager
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import us.minevict.mvutil.bungee.ext.readBungeeConfig
import us.minevict.mvutil.common.IMvPlugin
import us.minevict.mvutil.common.channel.IPacketChannel
import java.util.logging.Logger

/**
 * The base plugin for Bungee plugins using MV-Util.
 *
 * @since 5.0.0
 */
abstract class MvPlugin : Plugin(),
    IMvPlugin<BungeeCommandManager, Listener, MinevictusUtilsBungee> {
    override val platformLogger: Logger
        get() = logger

    private val acfDelegate = lazy {
        mvUtil.prepareAcf(BungeeCommandManager(this))
    }
    override val acf by acfDelegate

    private val databaseDelegate = lazy {
        mvUtil.prepareDatabase(databaseName, this)
    }
    override val database by databaseDelegate

    override val mvUtil: MinevictusUtilsBungee
        get() = MinevictusUtilsBungee.instance

    override lateinit var databaseName: String

    private var configuration: Configuration? = null
    private val channels = mutableListOf<IPacketChannel<*, *>>()
    private var errorState: IMvPlugin.PluginErrorState? = null
    var isEnabled = false
        private set

    override val pluginName: String
        get() = description.name

    override val version: String
        get() = description.version

    final override fun onLoad() {
        databaseName = pluginName.toLowerCase()

        try {
            if (!load()) {
                errorState = IMvPlugin.PluginErrorState.LOAD
                logger.warning("Disabling plugin...")
            }
        } catch (ex: Exception) {
            errorState = IMvPlugin.PluginErrorState.LOAD
            logger.severe("Encountered exception upon loading:")
            ex.printStackTrace()
            logger.warning("Disabling plugin...")
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
            } else {
                isEnabled = true
            }
        } catch (ex: Exception) {
            errorState = IMvPlugin.PluginErrorState.ENABLE
            logger.severe("Encountered exception upon enabling:")
            ex.printStackTrace()
            logger.warning("Disabling plugin...")
        }

        if (!isEnabled) {
            shutdownSafely()
        }
    }

    final override fun onDisable() {
        if (errorState != null) return

        shutdownSafely()

        try {
            disable()
        } catch (ex: Exception) {
            logger.severe("Encountered exception upon disabling:")
            ex.printStackTrace()
        }
    }

    private fun shutdownSafely() {
        isEnabled = false
        proxy.pluginManager.unregisterListeners(this)
        if (acfDelegate.isInitialized())
            acf.unregisterCommands()
        channels.forEach(IPacketChannel<*, *>::unregisterIncoming)
        if (databaseDelegate.isInitialized())
            database.close()
    }

    override fun listeners(vararg listeners: Listener) {
        listeners.forEach {
            proxy.pluginManager.registerListener(this, it)
        }
    }

    override fun <P, C : IPacketChannel<P, *>?> packetChannel(channel: C): C {
        channels.add(channel as IPacketChannel<*, *>)
        return channel
    }

    override fun registerCommands(vararg commands: BaseCommand) {
        commands.forEach(acf::registerCommand)
    }

    /**
     * Gets the configuration for this plugin. This calls [reloadConfig] if necessary.
     *
     * @return The current configuration.
     */
    fun getConfig(): Configuration {
        if (configuration == null) reloadConfig()
        return configuration!!
    }

    /**
     * Loads the configuration from file.
     */
    fun reloadConfig() {
        configuration = readBungeeConfig()
    }
}