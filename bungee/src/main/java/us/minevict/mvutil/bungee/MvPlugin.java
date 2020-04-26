package us.minevict.mvutil.bungee;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.LazyValue;

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 3.4.0
 */
@SuppressWarnings("RedundantThrows") // They exist to show what is allowed to be thrown.
public abstract class MvPlugin extends Plugin {
  private final LazyValue<BungeeCommandManager> acf = new LazyValue<>(this::constructAcf);
  private PluginErrorState errorState = null;
  private boolean enabled = false;

  @Override
  public final void onLoad() {
    try {
      if (!load()) {
        errorState = PluginErrorState.LOAD;
        getLogger().warning("Disabling plugin...");
      }
    } catch (Exception ex) {
      errorState = PluginErrorState.LOAD;
      getLogger().severe("Encountered exception upon loading:");
      ex.printStackTrace();
      getLogger().warning("Disabling plugin...");
    }

    if (!isEnabled()) {
      shutdownSafely();
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
      } else {
        enabled = true;
      }
    } catch (Exception ex) {
      errorState = PluginErrorState.ENABLE;
      getLogger().severe("Encountered exception upon enabling:");
      ex.printStackTrace();
      getLogger().warning("Disabling plugin...");
    }

    if (!isEnabled()) {
      shutdownSafely();
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

    enabled = false;
    shutdownSafely();

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
   * <p>
   * <ul>
   * <li>ACF will have no more commands by this stage.</li>
   * <li>The plugin has no more registered listeners by this stage.</li>
   * </ul>
   *
   * @throws Exception Any error encountered upon disabling.
   */
  protected void disable()
      throws Exception {
  }

  private void shutdownSafely() {
    getProxy().getPluginManager().unregisterListeners(this);
    acf.getIfInitialised().ifPresent(BungeeCommandManager::unregisterCommands);
  }

  /**
   * Gets the {@link MinevictusUtilsBungee MV-Util} instance.
   *
   * @return The {@link MinevictusUtilsBungee MV-Util} instance.
   */
  @NotNull
  public MinevictusUtilsBungee getMvUtil() {
    return MinevictusUtilsBungee.getInstance();
  }

  @NotNull
  private BungeeCommandManager constructAcf() {
    return getMvUtil().prepareAcf(new BungeeCommandManager(this));
  }

  /**
   * Gets a {@link BungeeCommandManager} linked to this plugin.
   * <p>
   * This has already been {@link MinevictusUtilsBungee#prepareAcf prepared} and is only constructed
   * once gotten.
   *
   * @return A newly constructed or cached {@link BungeeCommandManager} for this plugin.
   */
  @NotNull
  public final BungeeCommandManager getAcf() {
    return acf.getValue();
  }

  /**
   * Registers commands to this plugin's {@link BungeeCommandManager}.
   *
   * @param commands The commands to register.
   */
  public final void registerCommands(@NotNull BaseCommand... commands) {
    var acf = getAcf();
    for (var command : commands) {
      acf.registerCommand(command);
    }
  }

  /**
   * Gets whether this plugin is enabled.
   *
   * @return Whether this plugin is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  private enum PluginErrorState {
    LOAD,
    ENABLE,
  }
}
