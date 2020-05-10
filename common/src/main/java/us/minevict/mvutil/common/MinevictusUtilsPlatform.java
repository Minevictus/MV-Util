package us.minevict.mvutil.common;

import co.aikar.commands.CommandManager;
import co.aikar.idb.Database;
import io.lettuce.core.RedisClient;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A generic Minevictus utils platform.
 *
 * @param <CM> Command manager, dependent on platform.
 * @param <PL> Plugin class, dependent on platform.
 * @since 0.1.0
 */
public interface MinevictusUtilsPlatform<PL, CM extends CommandManager<?, ?, ?, ?, ?, ?>> {
  /**
   * Get the plugin's logger on the implemented platform.
   *
   * @return The plugin prefixed logger.
   * @since 0.1.0
   */
  @NotNull
  Logger getPlatformLogger();

  /**
   * Get the global {@link RedisClient}.
   *
   * @return The {@link RedisClient} shared by all plugins depending on this.
   * @since 3.7.0
   */
  @NotNull
  RedisClient getRedis();

  /**
   * Prepare the ACF command manager instance before it is used by plugins.
   *
   * @param commandManager The ACF command manager to use.
   * @return The same ACF command manager for chaining.
   */
  @NotNull
  CM prepareAcf(@NotNull CM commandManager);

  /**
   * Prepare a {@link Database} for any given {@link PL}.
   *
   * @param databaseName The name of the database you wish to use.
   * @param plugin       Plugin that requests the {@link Database}.
   * @return A new {@link Database} to use.
   * @since 4.0.0
   */
  @NotNull
  Database prepareDatabase(@NotNull String databaseName, @NotNull PL plugin);
}
