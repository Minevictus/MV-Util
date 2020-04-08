package us.minevict.mvutil.common;

import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A generic Minevictus utils platform.
 */
public interface MinevictusUtilsPlatform {
  /**
   * Get the plugin's logger on the implemented platform.
   *
   * @return The plugin prefixed logger.
   */
  @NotNull
  Logger getPlatformLogger();
}
