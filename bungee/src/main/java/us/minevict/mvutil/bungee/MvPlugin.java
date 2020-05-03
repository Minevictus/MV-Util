package us.minevict.mvutil.bungee;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import co.aikar.idb.Database;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.LazyValue;
import us.minevict.mvutil.common.acf.AcfCooldowns;

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 3.4.0
 */
@SuppressWarnings("RedundantThrows") // They exist to show what is allowed to be thrown.
public abstract class MvPlugin extends Plugin {
  private final LazyValue<BungeeCommandManager> acf = new LazyValue<>(this::constructAcf);
  private Configuration configuration = null;
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
   * Called upon disabling the plugin. This is not called if {@link #load()} or {@link #enable()} erred in some way.
   * <p>
   * <ul>
   * <li>ACF will have no more commands by this stage.</li>
   * <li>The plugin has no more registered listeners by this stage.</li>
   * </ul>
   * </p>
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
   * This has already been {@link MinevictusUtilsBungee#prepareAcf prepared} and is only constructed once gotten.
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
   * Register listeners for this plugin.
   *
   * @param listeners The listeners to register.
   */
  public void listeners(@NotNull Listener... listeners) {
    var pluginManager = getProxy().getPluginManager();
    for (var listener : listeners) {
      pluginManager.registerListener(this, listener);
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

  /**
   * Gets the configuration for this plugin. This calls {@link #reloadConfig()} if necessary.
   *
   * @return The current configuration.
   * @since 3.5.2
   */
  @NotNull
  public Configuration getConfig() {
    if (configuration == null) {
      reloadConfig();
    }

    return configuration;
  }

  /**
   * Loads the configuration from file.
   *
   * @since 3.5.2
   */
  public void reloadConfig() {
    getDataFolder().mkdirs();
    var configFile = new File(getDataFolder(), "config.yml");
    if (!configFile.isFile()) {
      try {
        Files.deleteIfExists(configFile.toPath());
        configFile.createNewFile();
        try (var input = getResourceAsStream("config.yml")) {
          if (input != null) {
            Files.copy(input, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
          } else {
            // There was no default configuration to put there.
            configuration = new Configuration();
            return;
          }
        }
      } catch (IOException ex) {
        throw new RuntimeException("Unable to create configuration file", ex);
      }
    }
    try {
      configuration = ConfigurationProvider
          .getProvider(YamlConfiguration.class)
          .load(configFile);
    } catch (IOException ex) {
      throw new RuntimeException("Unable to load configuration file", ex);
    }
  }

  /**
   * Saves the current configuration to file if it has been loaded.
   *
   * @since 3.5.2
   */
  public void saveConfig() {
    if (configuration == null) {
      // Nothing to save.
      return;
    }

    try {
      ConfigurationProvider
          .getProvider(YamlConfiguration.class)
          .save(configuration, new File(getDataFolder(), "config.yml"));
    } catch (IOException ex) {
      throw new RuntimeException("Unable to save configuration file", ex);
    }
  }

  /**
   * Set up the tables for these cooldowns.
   *
   * @param database  The database to set up the tables within.
   * @param cooldowns The cooldowns to setup tables for.
   * @since 3.6.0
   */
  public void setupCooldowns(@NotNull Database database, @NotNull AcfCooldowns[] cooldowns) {
    for (var cooldown : cooldowns) {
      cooldown.setupTable(database).exceptionally(ex -> {
        getLogger().warning("Exception when setting up cooldown");
        ex.printStackTrace();
        return null;
      });
    }
  }

  private enum PluginErrorState {
    LOAD,
    ENABLE,
  }
}
