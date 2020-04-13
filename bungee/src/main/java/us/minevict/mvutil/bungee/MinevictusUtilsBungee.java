package us.minevict.mvutil.bungee;

import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;
import us.minevict.mvutil.common.MvUtilVersion;

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
   */
  @NotNull
  public static MinevictusUtilsBungee getInstance() {
    return instance;
  }

  @Override
  public void onLoad() {
    try {
      //noinspection deprecation - Internal API warning.
      MvUtilVersion.setVersion(new MvUtilVersion(0, 1, 1));
    } catch (IllegalAccessException ex) {
      getLogger().severe("Cannot set the MV-Util version?");
      ex.printStackTrace();
      return;
    }
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
}
