package us.minevict.mvutil.spigot.internal

import co.aikar.idb.Database
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal object CloseDatabaseListener : Listener {
    val LOCK = Any()
    val databases = mutableMapOf<Plugin, Database>()

    @EventHandler(priority = EventPriority.MONITOR)
    fun disable(event: PluginDisableEvent) {
        val db = databases[event.plugin] ?: return
        event.plugin.logger.info("MV-Util closing database for plugin.")
        db.close()
    }
}