package us.minevict.mvutil.spigot;

import java.util.logging.Logger;
import me.tom.sparse.spigot.chat.menu.ChatMenuAPI;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;
import us.minevict.mvutil.spigot.hidden.MegaChunkSizes;
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
  public void onEnable() {
    saveDefaultConfig();
    getConfig().options().copyDefaults(true);

    getServer().getPluginManager().registerEvents(permissionsManager, this);

    MegaChunkSizes.MEGA_CHUNK_SIZE = getConfig().getInt("megachunk-size", MegaChunkSizes.MEGA_CHUNK_SIZE);
    MegaChunkSizes.MEGA_CHUNK_OFFSET_X = getConfig().getInt("megachunk-offset-x", MegaChunkSizes.MEGA_CHUNK_OFFSET_X);
    MegaChunkSizes.MEGA_CHUNK_OFFSET_Z = getConfig().getInt("megachunk-offset-z", MegaChunkSizes.MEGA_CHUNK_OFFSET_Z);

    ChatMenuAPI.init(this);
  }

  @Override
  public void onDisable() {
    ChatMenuAPI.disable();

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
