package us.minevict.mvutil.spigot

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import co.aikar.taskchain.BukkitTaskChainFactory
import co.aikar.taskchain.TaskChainFactory
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import us.minevict.mvutil.common.IMvPlugin
import us.minevict.mvutil.common.channel.IPacketChannel
import us.minevict.mvutil.spigot.permissions.PermissionsDSL
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 5.0.0
 */
abstract class MvPlugin : JavaPlugin(),
    IMvPlugin<PaperCommandManager, Listener, MinevictusUtilsSpigot> {
    override val platformLogger: Logger
        get() = logger

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
    private lateinit var taskChainFactory: TaskChainFactory

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
            shutdownSafely(false)
        }
    }

    final override fun onEnable() {
        if (errorState != null) return

        taskChainFactory = BukkitTaskChainFactory.create(this)

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
            shutdownSafely(true)
        }
    }

    final override fun onDisable() {
        if (errorState != null) return

        shutdownSafely(false)

        try {
            disable()
        } catch (ex: Exception) {
            logger.severe("Encountered exception upon disabling:")
            ex.printStackTrace()
        }

        taskChainFactory.shutdown(5, TimeUnit.SECONDS)
    }

    private fun shutdownSafely(doChain: Boolean) {
        if (doChain)
            taskChainFactory.shutdown(5, TimeUnit.SECONDS)
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
}