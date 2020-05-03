package us.minevict.mvutil.spigot.utils;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.HikariPooledDatabase;
import co.aikar.idb.PooledDatabaseOptions;
import java.util.Objects;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.spigot.hidden.CloseDatabaseListener;

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
      @NotNull Plugin plugin,
      @NotNull String user,
      @NotNull String pass,
      @NotNull String db,
      @NotNull String hostAndPort
  ) {
    Objects.requireNonNull(plugin, "the plugin cannot be null");
    Objects.requireNonNull(user, "the sql username cannot be null");
    Objects.requireNonNull(pass, "the sql password cannot be null");
    Objects.requireNonNull(db, "the sql database name cannot be null");
    Objects.requireNonNull(hostAndPort, "the sql host and port cannot be null");

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
   * Create an IDB {@link Database} instance to make SQL queries with the plugin as a base. This method will not set the
   * global {@link DB} to this by default and will close it when {@link PluginDisableEvent} is fired.
   *
   * @param plugin      {@link Plugin} to register listeners and get settings from.
   * @param user        SQL user.
   * @param pass        SQL password.
   * @param db          SQL database.
   * @param hostAndPort Host and port for the pool.
   * @return {@link Database} built around the {@link Plugin}.
   */
  public static Database createHikariDatabase(
      @NotNull Plugin plugin,
      @NotNull String user,
      @NotNull String pass,
      @NotNull String db,
      @NotNull String hostAndPort
  ) {
    Objects.requireNonNull(user, "the sql username cannot be null");
    Objects.requireNonNull(pass, "the sql password cannot be null");
    Objects.requireNonNull(db, "the sql database name cannot be null");
    Objects.requireNonNull(hostAndPort, "the sql host and port cannot be null");

    return createHikariDatabase(plugin, getRecommendedOptions(plugin, user, pass, db, hostAndPort));
  }

  /**
   * Create an IDB {@link Database} instance to make SQL queries with the plugin as a base. This method will not set the
   * global {@link DB} to this by default and will close it when {@link PluginDisableEvent} is fired.
   *
   * @param plugin  {@link Plugin} to register listeners and get settings from.
   * @param options {@link PooledDatabaseOptions} in case you don't want to use {@link #getRecommendedOptions(Plugin,
   *                String, String, String, String)}
   * @return {@link Database} using {@link Plugin} to register listeners.
   */
  public static Database createHikariDatabase(@NotNull Plugin plugin, @NotNull PooledDatabaseOptions options) {
    return createHikariDatabase(plugin, options, false);
  }

  /**
   * Create an IDB {@link Database} instance to make SQL queries with the plugin as a base. This is the basic builder
   * upon which you can decide if you don't want this database to be used globally.
   *
   * @param plugin    {@link Plugin} to register listeners and get settings from.
   * @param options   {@link PooledDatabaseOptions} in case you don't want to use {@link #getRecommendedOptions(Plugin,
   *                  String, String, String, String)}
   * @param setGlobal Whether you want this database to be used globally through {@link DB}.
   * @return {@link Database} using {@link Plugin} and your desired options.
   */
  public static Database createHikariDatabase(
      @NotNull Plugin plugin,
      @NotNull PooledDatabaseOptions options,
      boolean setGlobal
  ) {
    Objects.requireNonNull(plugin, "the plugin cannot be null");
    Objects.requireNonNull(options, "the options cannot be null");

    HikariPooledDatabase db = new HikariPooledDatabase(options);
    if (setGlobal) {
      DB.setGlobalDatabase(db);
    }

    CloseDatabaseListener.getInstance().getDatabases().put(plugin, db);
    return db;
  }
}
