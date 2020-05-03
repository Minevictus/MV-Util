package us.minevict.mvutil.common.acf;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandManager;
import co.aikar.idb.Database;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.minevict.mvutil.common.text.ComponentBuilderV2;
import us.minevict.mvutil.common.utils.Platformless;

/**
 * @since 0.1.0
 */
public class AcfFunctions {
  private AcfFunctions() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  /**
   * The default cooldown error message.
   *
   * @since 3.6.0
   */
  @NotNull
  public static final BiFunction<@NotNull CommandIssuer, @NotNull Long, @NotNull BaseComponent[]> DEFAULT_COOLDOWN_ERROR_MESSAGE =
      ($ignored, expiry) -> {
        long left = expiry - System.currentTimeMillis();
        return ComponentBuilderV2.of("This command is on cooldown for another ")
            .red()
            .duration(left, TimeUnit.MILLISECONDS)
            .yellow()
            .red(".")
            .create();
      };

  /**
   * Enables the help feature in the given {@link CommandManager}.
   *
   * @param commandManager The {@link CommandManager} to enable help for.
   */
  @SuppressWarnings("deprecation") // Developer warning
  public static void enableHelpFeature(CommandManager<?, ?, ?, ?, ?, ?> commandManager) {
    commandManager.enableUnstableAPI("help");
  }

  /**
   * Check whether the player executing this is on a cooldown.
   *
   * @param issuer            The {@link Issuer} of this command.
   * @param duration          The duration this command has a cooldown for in milliseconds. Use {@link TimeUnit} to have
   *                          easy calculation of this.
   * @param fetchLastExecuted Function to fetch last time the player executed the command or null if never.
   * @param setLastExecuted   Consumer to set the last time the player executed this command.
   * @param resetIfNotExpired Whether to always set the last time the player executed the command.
   * @param errorMessage      The error message to send players who are on cooldowns.
   * @param <Issuer>          The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      long duration,
      @NotNull Function<@NotNull Issuer, @Nullable Long> fetchLastExecuted,
      @NotNull BiConsumer<@NotNull Issuer, @NotNull Long> setLastExecuted,
      boolean resetIfNotExpired,
      @Nullable BiFunction<@NotNull Issuer, @NotNull Long, @NotNull BaseComponent[]> errorMessage
  ) {
    if (!issuer.isPlayer()) {
      return false;
    }

    var lastExecuted = fetchLastExecuted.apply(issuer);
    if (lastExecuted == null) {
      setLastExecuted.accept(issuer, System.currentTimeMillis());
      return false;
    }
    long now = System.currentTimeMillis();
    long expiry = now + duration;

    if (expiry <= now || resetIfNotExpired) {
      setLastExecuted.accept(issuer, System.currentTimeMillis());
    }
    if (expiry <= now) {
      return false;
    }

    if (errorMessage != null) {
      Platformless.sendMessage(issuer, errorMessage.apply(issuer, expiry));
    }

    return true;
  }

  /**
   * Check whether the player executing this is on a cooldown.
   * <p>
   * This does not verify threading and <b>will block the main thread</b> if not executed elsewhere.
   * </p>
   *
   * @param issuer            The {@link Issuer} of this command.
   * @param cooldown          The {@link AcfCooldowns} instance for this cooldown. The table must have been set up
   *                          already.
   * @param database          The {@link Database} to use for this cooldown check.
   * @param resetIfNotExpired Whether to always set the last time the player executed the command.
   * @param errorMessage      The error message to send players who are on cooldowns.
   * @param <Issuer>          The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  @SuppressWarnings("SqlResolve")
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      @NotNull AcfCooldowns cooldown,
      @NotNull Database database,
      boolean resetIfNotExpired,
      @Nullable BiFunction<@NotNull Issuer, @NotNull Long, @NotNull BaseComponent[]> errorMessage
  ) {
    return cooldown(
        issuer,
        cooldown.getDuration(issuer),
        i -> {
          try {
            var row = database.getFirstRow("SELECT lastExecuted FROM "
                + cooldown.getCooldownTableName() + " WHERE player = ?", i.getUniqueId().toString());
            if (row == null) {
              return null;
            }
            return row.getLong("lastExecuted");
          } catch (SQLException ex) {
            throw new RuntimeException(
                "exception while fetching last executed for cooldown "
                    + cooldown.getCooldownName(),
                ex
            );
          }
        },
        (i, newValue) -> {
          try {
            database.executeUpdate("INSERT INTO "
                    + cooldown.getCooldownTableName()
                    + " (player, lastExecuted) VALUES (?, ?) ON DUPLICATE KEY UPDATE lastExecuted = ?",
                i.getUniqueId().toString(), newValue, newValue
            );
          } catch (SQLException ex) {
            throw new RuntimeException(
                "exception while setting last executed for cooldown "
                    + cooldown.getCooldownName(),
                ex
            );
          }
        },
        resetIfNotExpired,
        errorMessage
    );
  }

  /**
   * Check whether the player executing this is on a cooldown.
   *
   * @param issuer            The {@link Issuer} of this command.
   * @param duration          The duration this command has a cooldown for in milliseconds. Use {@link TimeUnit} to have
   *                          easy calculation of this.
   * @param map               The map to store cooldown data in.
   * @param resetIfNotExpired Whether to always set the last time the player executed the command.
   * @param errorMessage      The error message to send players who are on cooldowns.
   * @param <Issuer>          The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      long duration,
      @NotNull Map<UUID, Long> map,
      boolean resetIfNotExpired,
      @Nullable BiFunction<@NotNull Issuer, @NotNull Long, @NotNull BaseComponent[]> errorMessage
  ) {
    return cooldown(
        issuer,
        duration,
        i -> map.get(i.getUniqueId()),
        (i, newValue) -> map.put(i.getUniqueId(), newValue),
        resetIfNotExpired,
        errorMessage
    );
  }

  /**
   * Check whether the player executing this is on a cooldown.
   * <p>
   * This does not verify threading and <b>will block the main thread</b> if not executed elsewhere.
   * </p>
   *
   * @param issuer       The {@link Issuer} of this command.
   * @param cooldown     The {@link AcfCooldowns} instance for this cooldown. The table must have been set up already.
   * @param database     The {@link Database} to use for this cooldown check.
   * @param errorMessage The error message to send players who are on cooldowns.
   * @param <Issuer>     The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      @NotNull AcfCooldowns cooldown,
      @NotNull Database database,
      @Nullable BiFunction<@NotNull Issuer, @NotNull Long, @NotNull BaseComponent[]> errorMessage
  ) {
    return cooldown(issuer, cooldown, database, false, errorMessage);
  }

  /**
   * Check whether the player executing this is on a cooldown.
   *
   * @param issuer       The {@link Issuer} of this command.
   * @param duration     The duration this command has a cooldown for in milliseconds. Use {@link TimeUnit} to have easy
   *                     calculation of this.
   * @param map          The map to store cooldown data in.
   * @param errorMessage The error message to send players who are on cooldowns.
   * @param <Issuer>     The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      long duration,
      @NotNull Map<UUID, Long> map,
      @Nullable BiFunction<@NotNull Issuer, @NotNull Long, @NotNull BaseComponent[]> errorMessage
  ) {
    return cooldown(issuer, duration, map, false, errorMessage);
  }

  /**
   * Check whether the player executing this is on a cooldown.
   * <p>
   * This does not verify threading and <b>will block the main thread</b> if not executed elsewhere.
   * </p>
   *
   * @param issuer   The {@link Issuer} of this command.
   * @param cooldown The {@link AcfCooldowns} instance for this cooldown. The table must have been set up already.
   * @param database The {@link Database} to use for this cooldown check.
   * @param <Issuer> The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      @NotNull AcfCooldowns cooldown,
      @NotNull Database database
  ) {
    return cooldown(
        issuer,
        cooldown,
        database,
        DEFAULT_COOLDOWN_ERROR_MESSAGE
    );
  }

  /**
   * Check whether the player executing this is on a cooldown.
   *
   * @param issuer   The {@link Issuer} of this command.
   * @param duration The duration this command has a cooldown for in milliseconds. Use {@link TimeUnit} to have easy
   *                 calculation of this.
   * @param map      The map to store cooldown data in.
   * @param <Issuer> The type of issuer for this command invocation.
   * @return Whether the player is currently on a cooldown.
   * @since 3.6.0
   */
  public static <Issuer extends CommandIssuer> boolean cooldown(
      @NotNull Issuer issuer,
      long duration,
      @NotNull Map<UUID, Long> map
  ) {
    return cooldown(
        issuer,
        duration,
        map,
        DEFAULT_COOLDOWN_ERROR_MESSAGE
    );
  }
}