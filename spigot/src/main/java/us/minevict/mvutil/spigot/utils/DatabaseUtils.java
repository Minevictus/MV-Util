package us.minevict.mvutil.spigot.utils;

import co.aikar.idb.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities ported over from Aikar's Bukkit IDB to manage DB creation for Bukkit on a simpler manner.
 *
 * @since 0.3.1
 */
public class DatabaseUtils {
  private DatabaseUtils() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  /**
   * {@link PooledDatabaseOptions} builder with the recommended options dialed in for a given {@link Plugin}.
   *
   * @param plugin      {@link Plugin} reference to extract preferences from.
   * @param user        SQL user.
   * @param pass        SQL password.
   * @param db          SQL database.
   * @param hostAndPort Host and port for the pool.
   * @return {@link PooledDatabaseOptions} built around the {@link Plugin}.
   */
  public static PooledDatabaseOptions getRecommendedOptions(
      Plugin plugin,
      @NotNull String user,
      @NotNull String pass,
      @NotNull String db,
      @NotNull String hostAndPort
  ) {
    DatabaseOptions options = DatabaseOptions
        .builder()
        .poolName(plugin.getDescription().getName() + " DB")
        .logger(plugin.getLogger())
        .mysql(user, pass, db, hostAndPort)
        .build();
    return PooledDatabaseOptions
        .builder()
        .options(options)
        .build();
  }

  /**
   * Create an IDB {@link Database} instance to make SQL queries with the plugin as a base.
   * This method will set the global {@link DB} to this by default and will close it when
   * {@link PluginDisableEvent} is fired.
   *
   *
   * @param plugin      {@link Plugin} to register listeners and get settings from.
   * @param user        SQL user.
   * @param pass        SQL password.
   * @param db          SQL database.
   * @param hostAndPort Host and port for the pool.
   * @return {@link Database} built around the {@link Plugin}.
   */
  public static Database createHikariDatabase(
      Plugin plugin,
      @NotNull String user,
      @NotNull String pass,
      @NotNull String db,
      @NotNull String hostAndPort
  ) {
    return createHikariDatabase(plugin, getRecommendedOptions(plugin, user, pass, db, hostAndPort));
  }

  /**
   * Create an IDB {@link Database} instance to make SQL queries with the plugin as a base.
   * This method will set the global {@link DB} to this by default and will close it when
   * {@link PluginDisableEvent} is fired.
   *
   * @param plugin      {@link Plugin} to register listeners and get settings from.
   * @param options     {@link PooledDatabaseOptions} in case you don't want to use {@link #getRecommendedOptions(Plugin, String, String, String, String)}
   * @return {@link Database} using {@link Plugin} to register listeners.
   */
  public static Database createHikariDatabase(Plugin plugin, PooledDatabaseOptions options) {
    return createHikariDatabase(plugin, options, true);
  }

  /**
   * Create an IDB {@link Database} instance to make SQL queries with the plugin as a base.
   * This is the basic builder upon which you can decide if you don't want this database to be used globally.
   *
   * @param plugin      {@link Plugin} to register listeners and get settings from.
   * @param options     {@link PooledDatabaseOptions} in case you don't want to use {@link #getRecommendedOptions(Plugin, String, String, String, String)}
   * @param setGlobal   Whether you want this database to be used globally through {@link DB}.
   * @return {@link Database} using {@link Plugin} and your desired options.
   */
  public static Database createHikariDatabase(Plugin plugin, PooledDatabaseOptions options, boolean setGlobal) {
    HikariPooledDatabase db = new HikariPooledDatabase(options);
    if (setGlobal) {
      DB.setGlobalDatabase(db);
    }

    plugin.getServer().getPluginManager().registerEvents(new Listener() {
      @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
      public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
          db.close();
        }
      }
    }, plugin);
    return db;
  }
}
