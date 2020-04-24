package us.minevict.mvutil.bungee;

import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.MessageType;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.bungee.utils.Functions;
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
}
