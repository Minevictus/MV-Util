package us.minevict.mvutil.common;

import io.lettuce.core.RedisClient;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A generic Minevictus utils platform.
 *
 * @since 0.1.0
 */
public interface MinevictusUtilsPlatform {
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
}
