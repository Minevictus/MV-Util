package us.minevict.mvutil.spigot;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.LazyValue;

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 3.4.0
 */
@SuppressWarnings("RedundantThrows") // They exist to show what is allowed to be thrown.
public abstract class MvPlugin extends JavaPlugin {
  private final LazyValue<PaperCommandManager> acf = new LazyValue<>(this::constructAcf);
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

    HandlerList.unregisterAll(this);
    acf.getIfInitialised().ifPresent(PaperCommandManager::unregisterCommands);

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

  /**
   * Gets the {@link MinevictusUtilsSpigot MV-Util} instance.
   *
   * @return The {@link MinevictusUtilsSpigot MV-Util} instance.
   */
  @NotNull
  public MinevictusUtilsSpigot getMvUtil() {
    return MinevictusUtilsSpigot.getInstance();
  }

  @NotNull
  private PaperCommandManager constructAcf() {
    return getMvUtil().prepareAcf(new PaperCommandManager(this));
  }

  /**
   * Gets a {@link PaperCommandManager} linked to this plugin.
   * <p>
   * This has already been {@link MinevictusUtilsSpigot#prepareAcf prepared} and is only constructed
   * once gotten.
   *
   * @return A newly constructed or cached {@link PaperCommandManager} for this plugin.
   */
  @NotNull
  public final PaperCommandManager getAcf() {
    return acf.getValue();
  }

  /**
   * Registers commands to this plugin's {@link PaperCommandManager}.
   * <p>
   * This overwrites other commands in the same names by default.
   *
   * @param commands The commands to register.
   */
  public final void registerCommands(@NotNull BaseCommand... commands) {
    registerCommands(true, commands);
  }

  /**
   * Registers commands to this plugin's {@link PaperCommandManager}.
   *
   * @param force    Whether to overwrite other commands in the same names.
   * @param commands The commands to register.
   */
  public final void registerCommands(boolean force, @NotNull BaseCommand... commands) {
    var acf = getAcf();
    for (var command : commands) {
      acf.registerCommand(command, force);
    }
  }

  private enum PluginErrorState {
    LOAD,
    ENABLE,
  }
}
