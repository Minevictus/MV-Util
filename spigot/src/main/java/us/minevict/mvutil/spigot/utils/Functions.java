package us.minevict.mvutil.spigot.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * General functions which do not have a place anywhere else.
 *
 * @since 3.3.1
 */
public final class Functions {
  private Functions() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated");
  }

  /**
   * Copy a resource from the plugin's classpath to the destination.
   *
   * @param plugin The {@link Plugin} to copy for.
   * @param name   The name of the file in the classpath; this is a path such as
   *               <code>configs/test.yml</code>.
   * @return Whether the copying was successful.
   * @throws IOException              Thrown if an error happens while copying the resource and failsafe is false.
   * @throws IllegalStateException    Thrown if there is a directory at the destination regardless of failsafe.
   * @throws IllegalArgumentException Thrown if there is no resource under the name given and failsafe is false.
   * @see #copyResource(Plugin, String, File, boolean, boolean)
   */
  public static boolean copyResource(
      @NotNull Plugin plugin,
      @NotNull String name
  ) throws IOException {
    return copyResource(plugin, name, new File(plugin.getDataFolder(), name));
  }

  /**
   * Copy a resource from the plugin's classpath to the destination.
   *
   * @param plugin      The {@link Plugin} to copy for.
   * @param name        The name of the file in the classpath; this is a path such as
   *                    <code>configs/test.yml</code>.
   * @param destination The destination {@link File}.
   * @return Whether the copying was successful.
   * @throws IOException              Thrown if an error happens while copying the resource and failsafe is false.
   * @throws IllegalStateException    Thrown if there is a directory at the destination regardless of failsafe.
   * @throws IllegalArgumentException Thrown if there is no resource under the name given and failsafe is false.
   * @see #copyResource(Plugin, String, File, boolean, boolean)
   */
  public static boolean copyResource(
      @NotNull Plugin plugin,
      @NotNull String name,
      @NotNull File destination
  ) throws IOException {
    return copyResource(plugin, name, destination, false);
  }

  /**
   * Copy a resource from the plugin's classpath to the destination.
   *
   * @param plugin      The {@link Plugin} to copy for.
   * @param name        The name of the file in the classpath; this is a path such as
   *                    <code>configs/test.yml</code>.
   * @param destination The destination {@link File}.
   * @param overwrite   Whether to overwrite the destination if it already exists as a file.
   * @return Whether the copying was successful.
   * @throws IOException              Thrown if an error happens while copying the resource and failsafe is false.
   * @throws IllegalStateException    Thrown if there is a directory at the destination regardless of failsafe.
   * @throws IllegalArgumentException Thrown if there is no resource under the name given and failsafe is false.
   * @see #copyResource(Plugin, String, File, boolean, boolean)
   */
  public static boolean copyResource(
      @NotNull Plugin plugin,
      @NotNull String name,
      @NotNull File destination,
      boolean overwrite
  ) throws IOException {
    return copyResource(plugin, name, destination, overwrite, false);
  }

  /**
   * Copy a resource from the plugin's classpath to the destination.
   *
   * @param plugin      The {@link Plugin} to copy for.
   * @param name        The name of the file in the classpath; this is a path such as
   *                    <code>configs/test.yml</code>.
   * @param destination The destination {@link File}.
   * @param overwrite   Whether to overwrite the destination if it already exists as a file.
   * @param failSafe    Whether to return false if anything goes wrong during the copy.
   * @return Whether the copying was successful.
   * @throws IOException              Thrown if an error happens while copying the resource and failsafe is false.
   * @throws IllegalStateException    Thrown if there is a directory at the destination regardless of failsafe.
   * @throws IllegalArgumentException Thrown if there is no resource under the name given and failsafe is false.
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static boolean copyResource(
      @NotNull Plugin plugin,
      @NotNull String name,
      @NotNull File destination,
      boolean overwrite,
      boolean failSafe
  ) throws IOException {
    Objects.requireNonNull(plugin, "the plugin cannot be null");
    Objects.requireNonNull(name, "the filename in the classpath cannot be null");
    Objects.requireNonNull(destination, "the destination file cannot be null");

    destination.getParentFile().mkdirs();

    if (!overwrite && destination.isFile()) {
      return false;
    }
    if (destination.isDirectory()) {
      // Interesting - let's fail here.
      throw new IllegalStateException(
          "there is already a directory at: " + destination.getAbsolutePath());
    }

    try (var stream = plugin.getResource(name)) {
      if (stream == null) {
        if (failSafe) {
          return false;
        }
        throw new IllegalArgumentException("no filename in classpath: " + name);
      }

      Files.copy(
          stream,
          destination.toPath(),
          StandardCopyOption.REPLACE_EXISTING
      );

      return true;
    } catch (IOException ex) {
      if (failSafe) {
        return false;
      }
      throw ex;
    }
  }
}
