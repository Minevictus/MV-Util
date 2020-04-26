package us.minevict.mvutil.spigot;

import org.bukkit.plugin.java.JavaPlugin;
import us.minevict.mvutil.spigot.hidden.PluginErrorState;

@SuppressWarnings("RedundantThrows") // They exist to show what is allowed to be thrown.
public abstract class MvPlugin extends JavaPlugin {
  private PluginErrorState errorState = null;

  @Override
  public final void onLoad() {
    try {
      if (!load()) {
        errorState = PluginErrorState.LOAD;
        getLogger().warning("Disabling plugin...");
        setEnabled(false);
      }
    } catch (Exception ex) {
      errorState = PluginErrorState.LOAD;
      getLogger().severe("Encountered exception upon loading:");
      ex.printStackTrace();
      getLogger().warning("Disabling plugin...");
      setEnabled(false);
    }
  }

  /**
   * Called upon loading the plugin.
   * <p>
   * Returns true if the plugin successfully loads, false otherwise.
   *
   * @return Whether the load was successful.
   * @throws Exception Any error encountered upon loading.
   */
  protected boolean load()
      throws Exception {
    return true;
  }

  @Override
  public final void onEnable() {
    if (errorState != null) {
      return;
    }

    try {
      if (!enable()) {
        errorState = PluginErrorState.ENABLE;
        getLogger().warning("Disabling plugin...");
        setEnabled(false);
      }
    } catch (Exception ex) {
      errorState = PluginErrorState.ENABLE;
      getLogger().severe("Encountered exception upon enabling:");
      ex.printStackTrace();
      getLogger().warning("Disabling plugin...");
      setEnabled(false);
    }
  }

  /**
   * Called upon enabling the plugin. This is not called if {@link #load()} erred in some way.
   * <p>
   * Returns true if the plugin successfully enables, false otherwise.
   *
   * @return Whether the enable was successful.
   * @throws Exception Any error encountered upon enabling.
   */
  protected boolean enable()
      throws Exception {
    return true;
  }

  @Override
  public final void onDisable() {
    if (errorState != null) {
      return;
    }

    try {
      disable();
    } catch (Exception ex) {
      getLogger().severe("Encountered exception upon disabling:");
      ex.printStackTrace();
    }
  }

  /**
   * Called upon disabling the plugin. This is not called if {@link #load()} or {@link #enable()}
   * erred in some way.
   *
   * @throws Exception Any error encountered upon disabling.
   */
  protected void disable()
      throws Exception {
  }
}
