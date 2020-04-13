package us.minevict.mvutil.spigot.utils;

import com.proximyst.mvnms.MvNms;
import org.bukkit.Bukkit;

/**
 * @since 0.1.0
 */
public class PluginUtils {
  private PluginUtils() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  /**
   * Gets whether the MV-NMS plugin is currently present.
   *
   * @return Whether MV-NMS is present and enabled.
   */
  public static boolean isMvNmsPresent() {
    var plugin = Bukkit.getPluginManager().getPlugin("MV-NMS");

    return plugin != null
        && plugin.isEnabled()
        && plugin instanceof MvNms;
  }
}