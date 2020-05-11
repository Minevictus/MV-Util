package us.minevict.mvutil.spigot.utils

import com.proximyst.mvnms.MvNms
import org.bukkit.Bukkit

/**
 * Utilities regarding plugins this plugin can depend on.
 *
 * @since 0.1.0
 */
object PluginUtils {
    /**
     * Gets whether the MV-NMS plugin is currently present.
     *
     * @return Whether MV-NMS is present and enabled.
     */
    fun isMvNmsPresent(): Boolean =
        Bukkit.getPluginManager().getPlugin("MV-NMS")?.let {
            it.isEnabled && it is MvNms
        } == true
}