package us.minevict.mvutil.spigot;

import java.util.logging.Logger;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;
import us.minevict.mvutil.spigot.permissions.PermissionsManager;

public class MinevictusUtilsSpigot
    extends JavaPlugin
    implements MinevictusUtilsPlatform {
  @NotNull
  private final PermissionsManager permissionsManager;

  public MinevictusUtilsSpigot() {
    super();

    this.permissionsManager = new PermissionsManager(this);
  }

  @Override
  public void onLoad() {
    getServer().getPluginManager().registerEvents(permissionsManager, this);
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
  }

  @Override
  @NotNull
  public Logger getPlatformLogger() {
    return getLogger();
  }

  /**
   * Gets the current instance of this plugin.
   */
  @NotNull
  public static MinevictusUtilsSpigot getInstance() {
    return getPlugin(MinevictusUtilsSpigot.class);
  }

  /**
   * Gets the permissions manager for managing a dependent's permissions.
   */
  @NotNull
  public PermissionsManager getPermissionsManager() {
    return permissionsManager;
  }
}
