package us.minevict.mvutil.bungee;

import co.aikar.commands.BungeeCommandManager;
import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;

public class MinevictusUtilsBungee
    extends Plugin
    implements MinevictusUtilsPlatform {
  @SuppressWarnings("NotNullFieldNotInitialized")
  @NotNull
  private static MinevictusUtilsBungee instance;

  public MinevictusUtilsBungee() {
    super();

    instance = this;
  }

  /**
   * Gets the current instance of this plugin.
   *
   * @return The current instance of this plugin.
   */
  @NotNull
  public static MinevictusUtilsBungee getInstance() {
    return instance;
  }

  @Override
  public void onDisable() {
    getProxy().getPluginManager().unregisterListeners(this);
    getProxy().getPluginManager().unregisterCommands(this);

    //noinspection ConstantConditions
    instance = null;
  }

  @Override
  @NotNull
  public Logger getPlatformLogger() {
    return getLogger();
  }

  /**
   * Prepare a command manager.
   *
   * @param commandManager The command manager to prepare.
   * @return The command managed for use in chaining or the likes.
   * @since 3.3.0
   */
  @NotNull
  public BungeeCommandManager prepareAcf(@NotNull BungeeCommandManager commandManager) {
    return commandManager;
  }
}
