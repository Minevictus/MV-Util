package us.minevict.mvutil.common;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandManager;
import co.aikar.idb.Database;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.acf.AcfCooldowns;
import us.minevict.mvutil.common.channel.IPacketChannel;

/**
 * Shared interface by MvPlugin integrations on different platforms.
 *
 * @since 4.0.0
 */
public interface IMvPlugin<
    CM extends CommandManager<?, ?, ?, ?, ?, ?>,
    L,
    PF extends MinevictusUtilsPlatform<?, CM>
    > {
  /**
   * Called upon loading the plugin.
   * <p>
   * Returns true if the plugin successfully loads, false otherwise.
   *
   * @return Whether the load was successful.
   * @throws Exception Any error encountered upon loading.
   */
  default boolean load() throws Exception {
    return true;
  }

  /**
   * Called upon enabling the plugin. This is not called if {@link #load()} erred in some way.
   * <p>
   * Returns true if the plugin successfully enables, false otherwise.
   *
   * @return Whether the enable was successful.
   * @throws Exception Any error encountered upon enabling.
   */
  default boolean enable() throws Exception {
    return true;
  }

  /**
   * Called upon disabling the plugin. This is not called if {@link #load()} or {@link #enable()} erred in some way.
   * <br>
   * <ul>
   * <li>ACF will have no more commands by this stage.</li>
   * <li>The plugin has no more registered listeners by this stage.</li>
   * <li>The plugin has all its tasks cancelled by this stage.</li>
   * <li>The task chain factory will shut down after this has been called.</li>
   * </ul>
   *
   * @throws Exception Any error encountered upon disabling.
   */
  default void disable() throws Exception {
  }

  /**
   * Gets the {@link MinevictusUtilsPlatform MV-Util} instance.
   *
   * @return The {@link MinevictusUtilsPlatform MV-Util} implemented instance.
   */
  @NotNull
  PF getMvUtil();

  /**
   * Gets a {@link CommandManager} linked to this plugin.
   * <p>
   * This has already been {@link MinevictusUtilsPlatform#prepareAcf(CommandManager)  prepared} and is only constructed
   * once gotten.
   *
   * @return A newly constructed or cached {@link CommandManager} for this plugin.
   */
  @NotNull
  CM getAcf();

  /**
   * Gets a {@link Database} linked to this plugin.
   * <p>
   * For the database name we will use the plugin name, replacing spaces for underscores and lower casing it.
   *
   * @return A newly constructed or cached {@link Database} for this plugin.
   * @since 4.0.0
   */
  @NotNull
  Database getDatabase();

  /**
   * Get the name of the database the plugin will connect to if necessary.
   *
   * @return Database name.
   */
  @NotNull
  String getDatabaseName();

  /**
   * Set the name of the database the plugin will connect to if necessary.
   *
   * @param databaseName The new database name.
   * @return Database name.
   */
  @NotNull
  void setDatabaseName(String databaseName);

  /**
   * Registers commands to this plugin's {@link CommandManager}.
   * <p>
   * This overwrites other commands in the same names by default.
   *
   * @param commands The commands to register.
   */
  void registerCommands(@NotNull BaseCommand... commands);

  /**
   * Register listeners for this plugin.
   *
   * @param listeners The listeners to register.
   */
  void listeners(@NotNull L... listeners);

  /**
   * Set up the tables for these cooldowns.
   *
   * @param database  The database to set up the tables within.
   * @param cooldowns The cooldowns to setup tables for.
   */
  void setupCooldowns(@NotNull Database database, @NotNull AcfCooldowns[] cooldowns);

  /**
   * Register a packet channel and handle its unregistering.
   *
   * @param channel The channel to register and handle.
   * @param <P>     The type of packet for the channel to handle.
   * @param <C>     The type of the channel.
   * @return The channel given.
   * @since 3.8.0
   */
  @NotNull <P, C extends IPacketChannel<P, ?>> C packetChannel(@NotNull C channel);

  /**
   * Get the plugin's name.
   *
   * @return {@link IMvPlugin Plugin's} name
   */
  @NotNull
  String getName();

  /**
   * Get the plugin's current version.
   *
   * @return The current version of the plugin.
   */
  @NotNull
  String getVersion();

  enum PluginErrorState {
    LOAD,
    ENABLE,
  }
}
