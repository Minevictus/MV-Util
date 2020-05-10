package us.minevict.mvutil.bungee;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import co.aikar.idb.Database;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.IMvPlugin;
import us.minevict.mvutil.common.LazyValue;
import us.minevict.mvutil.common.acf.AcfCooldowns;
import us.minevict.mvutil.common.channel.IPacketChannel;

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 3.4.0
 */
public abstract class MvPlugin
    extends Plugin
    implements IMvPlugin<BungeeCommandManager, Listener, MinevictusUtilsBungee> {
  private final LazyValue<BungeeCommandManager> acf = new LazyValue<>(this::constructAcf);
  private final List<IPacketChannel<?, ?>> channels = new ArrayList<>();
  private Configuration configuration = null;
  private PluginErrorState errorState = null;
  private boolean enabled = false;
  private String databaseName;
  private final LazyValue<Database> database = new LazyValue<>(this::constructDatabase);

  @Override
  public final void onLoad() {
    databaseName = getName().toLowerCase();

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

  private void shutdownSafely() {
    getProxy().getPluginManager().unregisterListeners(this);
    acf.getIfInitialised().ifPresent(BungeeCommandManager::unregisterCommands);
    database.getIfInitialised().ifPresent(Database::close);
    channels.forEach(IPacketChannel::unregisterIncoming);
  }

  /**
   * Gets the {@link MinevictusUtilsBungee MV-Util} instance.
   *
   * @return The {@link MinevictusUtilsBungee MV-Util} instance.
   */
  @Override
  @NotNull
  public MinevictusUtilsBungee getMvUtil() {
    return MinevictusUtilsBungee.getInstance();
  }

  @NotNull
  private BungeeCommandManager constructAcf() {
    return getMvUtil().prepareAcf(new BungeeCommandManager(this));
  }

  /**
   * {@inheritDoc}
   */
  @NotNull
  private Database constructDatabase() {
    return getMvUtil().prepareDatabase(this.databaseName, this);
  }

  /**
   * Gets a {@link BungeeCommandManager} linked to this plugin.
   * <p>
   * This has already been {@link MinevictusUtilsBungee#prepareAcf prepared} and is only constructed once gotten.
   *
   * @return A newly constructed or cached {@link BungeeCommandManager} for this plugin.
   */
  @Override
  @NotNull
  public final BungeeCommandManager getAcf() {
    return acf.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public final Database getDatabase() {
    return database.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void registerCommands(@NotNull BaseCommand... commands) {
    var acf = getAcf();
    for (var command : commands) {
      acf.registerCommand(command);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void listeners(@NotNull Listener... listeners) {
    var pluginManager = getProxy().getPluginManager();
    for (var listener : listeners) {
      pluginManager.registerListener(this, listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getVersion() {
    return getDescription().getVersion();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getName() {
    return getDescription().getName();
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
  public final Configuration getConfig() {
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
  public final void reloadConfig() {
    getDataFolder().mkdirs();
    var configFile = new File(getDataFolder(), "config.yml");
    if (!configFile.isFile()) {
      try {
        Files.deleteIfExists(configFile.toPath());
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
  public final void saveConfig() {
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
   * {@inheritDoc}
   */
  @Override
  public void setupCooldowns(@NotNull Database database, @NotNull AcfCooldowns[] cooldowns) {
    for (var cooldown : cooldowns) {
      cooldown.setupTable(database).exceptionally(ex -> {
        getLogger().warning("Exception when setting up cooldown");
        ex.printStackTrace();
        return null;
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public final <P, C extends IPacketChannel<P, ?>> C packetChannel(@NotNull C channel) {
    channels.add(channel);
    return channel;
  }
}
