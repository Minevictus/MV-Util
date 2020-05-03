package us.minevict.mvutil.common.acf;

import co.aikar.commands.CommandIssuer;
import co.aikar.idb.Database;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.utils.Platformless;

/**
 * Marker for which cooldowns are present in ACF.
 *
 * @since 3.6.0
 */
public interface AcfCooldowns {
  /**
   * Get the duration in milliseconds specific to this issuer and this cooldown.
   *
   * @param issuer The issuer to get the duration of.
   * @return The duration for this issuer in milliseconds.
   */
  long getDuration(CommandIssuer issuer);

  /**
   * The name of this cooldown. It is safe to just return {@link Enum#name()} here.
   *
   * @return The name of this cooldown.
   */
  default String getCooldownName() {
    if (this instanceof Enum) {
      return ((Enum<?>) this).name();
    }
    return this.getClass().getSimpleName();
  }

  /**
   * Set up the table for this cooldown.
   * <p>
   * There must be a column called <code>player</code> with the type <code>char(36)</code>. There must be a column
   * called <code>lastExecuted</code> with the type <code>long</code>.
   * </p>
   *
   * @param database The database to use for the data.
   * @return A future which completes once the table is set up.
   */
  default CompletableFuture<Void> setupTable(@NotNull Database database) {
    return Platformless.runAsync(() -> {
      var tableName = getCooldownTableName();
      try {
        database.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " ("
            + "player CHAR(36) not null,"
            + "lastExecuted BIGINT not null default 0,"
            + "primary key(player)"
            + ")");
      } catch (SQLException ex) {
        throw new RuntimeException("error setting up cooldowns table " + tableName, ex);
      }
      return null;
    });
  }

  /**
   * Get the table name for this cooldown.
   *
   * @return The table name for this cooldown.
   */
  default String getCooldownTableName() {
    return "cooldowns_" + getCooldownName();
  }
}
