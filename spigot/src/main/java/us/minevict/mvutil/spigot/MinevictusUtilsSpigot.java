package us.minevict.mvutil.spigot;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;
import us.minevict.mvutil.common.MvUtilVersion;
import us.minevict.mvutil.spigot.permissions.PermissionsManager;

import java.util.logging.Logger;

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
    try {
      //noinspection deprecation - Internal API warning.
      MvUtilVersion.setVersion(new MvUtilVersion(0, 2, 1));
    } catch (IllegalAccessException ex) {
      getLogger().severe("Cannot set the MV-Util version?");
      ex.printStackTrace();
      setEnabled(false);
      return;
    }
  }

  @Override
  public void onEnable() {
    saveDefaultConfig();
    getConfig().options().copyDefaults(true);

    getServer().getPluginManager().registerEvents(permissionsManager, this);

    MegaChunk.MEGA_CHUNK_SIZE = getConfig().getInt("megachunk-size", MegaChunk.MEGA_CHUNK_SIZE);
    MegaChunk.MEGA_CHUNK_OFFSET_X = getConfig().getInt("megachunk-offset-x", MegaChunk.MEGA_CHUNK_OFFSET_X);
    MegaChunk.MEGA_CHUNK_OFFSET_Z = getConfig().getInt("megachunk-offset-z", MegaChunk.MEGA_CHUNK_OFFSET_Z);
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
   *
   * @return current instance of this plugin.
   */
  @NotNull
  public static MinevictusUtilsSpigot getInstance() {
    return getPlugin(MinevictusUtilsSpigot.class);
  }

  /**
   * Gets the permissions manager for managing a dependent's permissions.
   *
   * @return {@link PermissionsManager} for this instance.
   */
  @NotNull
  public PermissionsManager getPermissionsManager() {
    return permissionsManager;
  }
}
