package us.minevict.mvutil.bungee;

import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.MessageType;
import co.aikar.idb.Database;
import co.aikar.idb.PooledDatabaseOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.bungee.utils.DatabaseUtils;
import us.minevict.mvutil.bungee.utils.Functions;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;
import us.minevict.mvutil.common.utils.SetupPlatformless;

/**
 * @since 0.1.0
 */
public class MinevictusUtilsBungee
    extends Plugin
    implements MinevictusUtilsPlatform<Plugin, BungeeCommandManager> {
  @SuppressWarnings("NotNullFieldNotInitialized")
  @NotNull
  private static MinevictusUtilsBungee instance;
  private RedisClient redisClient;
  private Configuration configuration;

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
  public void onLoad() {
    SetupPlatformless.setup(this);
  }

  @Override
  public void onEnable() {
    readConfig();

    redisClient = RedisClient.create(RedisURI.builder()
        .withHost(configuration.getString("redis-hostname"))
        .withPort(configuration.getInt("redis-port"))
        .build());
  }

  @Override
  public void onDisable() {
    getProxy().getPluginManager().unregisterListeners(this);
    getProxy().getPluginManager().unregisterCommands(this);

    redisClient.shutdown(2, 5, TimeUnit.SECONDS);

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
  @Override
  @NotNull
  public BungeeCommandManager prepareAcf(@NotNull BungeeCommandManager commandManager) {
    // Why the FUCK is MessageType not an enum??
    for (var type : new MessageType[]{
        MessageType.ERROR,
        MessageType.HELP,
        MessageType.INFO,
        MessageType.SYNTAX
    }) {
      for (var colour : ChatColor.values()) {
        commandManager.setFormat(type, colour.ordinal(), colour);
      }
    }

    var acfLangFile = new File(commandManager.getPlugin().getDataFolder(), "acf-lang-en.yml");
    try {
      Functions.copyResource(
          this,
          "acf-lang-en.yml",
          acfLangFile
      );

      commandManager.getLocales().loadYamlLanguageFile(acfLangFile, Locale.ENGLISH);
    } catch (IOException ex) {
      getLogger().warning("Could not save & load ACF language file for "
          + commandManager.getPlugin().getDescription().getName());
      ex.printStackTrace();
    }

    return commandManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Database prepareDatabase(@NotNull String databaseName, @NotNull Plugin plugin) {
    Objects.requireNonNull(plugin, "plugin cannot be null");

    PooledDatabaseOptions options = DatabaseUtils.getRecommendedOptions(
        plugin,
        Objects.requireNonNull(configuration.getString("sql-username"), "user cannot be null"),
        Objects.requireNonNull(configuration.getString("sql-password"), "password cannot be null"),
        Objects.requireNonNull(databaseName, "database cannot be null"),
        Objects.requireNonNull(configuration.getString("sql-host-and-port"), "host and port cannot be null")
    );
    return DatabaseUtils.createHikariDatabase(plugin, options, false);
  }

  private void readConfig() {
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

  @Override
  @NotNull
  public RedisClient getRedis() {
    return redisClient;
  }
}
