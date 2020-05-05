package us.minevict.mvutil.spigot;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import me.tom.sparse.spigot.chat.menu.ChatMenuAPI;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.MinevictusUtilsPlatform;
import us.minevict.mvutil.common.utils.SetupPlatformless;
import us.minevict.mvutil.spigot.hidden.CloseDatabaseListener;
import us.minevict.mvutil.spigot.hidden.MegaChunkSizes;
import us.minevict.mvutil.spigot.permissions.PermissionsManager;
import us.minevict.mvutil.spigot.utils.Functions;

/**
 * @since 0.1.0
 */
public class MinevictusUtilsSpigot
    extends JavaPlugin
    implements MinevictusUtilsPlatform {
  @NotNull
  private final PermissionsManager permissionsManager;
  private RedisClient redisClient;

  public MinevictusUtilsSpigot() {
    super();

    this.permissionsManager = new PermissionsManager(this);
  }

  @Override
  public void onLoad() {
    SetupPlatformless.setup(this);
  }

  @Override
  public void onEnable() {
    saveDefaultConfig();
    getConfig().options().copyDefaults(true);

    redisClient = RedisClient.create(RedisURI.builder()
        .withHost(getConfig().getString("redis-hostname"))
        .withPort(getConfig().getInt("redis-port"))
        .build());

    getServer().getPluginManager().registerEvents(permissionsManager, this);
    getServer().getPluginManager().registerEvents(CloseDatabaseListener.getInstance(), this);

    MegaChunkSizes.MEGA_CHUNK_SIZE = getConfig()
        .getInt("megachunk-size", MegaChunkSizes.MEGA_CHUNK_SIZE);
    MegaChunkSizes.MEGA_CHUNK_OFFSET_X = getConfig()
        .getInt("megachunk-offset-x", MegaChunkSizes.MEGA_CHUNK_OFFSET_X);
    MegaChunkSizes.MEGA_CHUNK_OFFSET_Z = getConfig()
        .getInt("megachunk-offset-z", MegaChunkSizes.MEGA_CHUNK_OFFSET_Z);

    if (getConfig().getBoolean("megachunk-random-offset-x")) {
      MegaChunkSizes.MEGA_CHUNK_OFFSET_X += ThreadLocalRandom.current().nextInt();
    }
    if (getConfig().getBoolean("megachunk-random-offset-z")) {
      MegaChunkSizes.MEGA_CHUNK_OFFSET_Z += ThreadLocalRandom.current().nextInt();
    }

    if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
      ChatMenuAPI.init(this);
    }
  }

  @Override
  public void onDisable() {
    ChatMenuAPI.disable();

    HandlerList.unregisterAll(this);
    redisClient.shutdown(2, 5, TimeUnit.SECONDS);
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

  /**
   * Prepare a command manager.
   *
   * @param commandManager The command manager to prepare.
   * @return The command managed for use in chaining or the likes.
   * @since 3.3.0
   */
  @NotNull
  public PaperCommandManager prepareAcf(@NotNull PaperCommandManager commandManager) {
    // Why the FUCK is MessageType not an enum??
    for (var type : new MessageType[]{
        MessageType.ERROR,
        MessageType.HELP,
        MessageType.INFO,
        MessageType.SYNTAX
    }) {
      for (var colour : org.bukkit.ChatColor.values()) {
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
    } catch (IOException | InvalidConfigurationException ex) {
      getLogger().warning("Could not save & load ACF language file for "
          + commandManager.getPlugin().getDescription().getName());
      ex.printStackTrace();
    }

    return commandManager;
  }

  @Override
  @NotNull
  public RedisClient getRedis() {
    return redisClient;
  }
}
