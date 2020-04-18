package us.minevict.mvutil.spigot.funkychunk;

import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * This defines a funky-chunk potentially within a world.
 *
 * @since 0.2.4
 */
public interface FunkyChunk {
  /**
   * Gets the {@link World} for this {@link FunkyChunk}.
   * <p>
   * This is not necessarily available. If one is not available, all methods checking containment will ignore the {@link
   * World}.
   *
   * @return An {@link Optional} wrapped {@link World} for this {@link FunkyChunk}, or null.
   */
  @NotNull
  Optional<World> getWorld();

  /**
   * Checks whether the given {@link Location} is/can be within this {@link FunkyChunk}.
   *
   * @param location The {@link Location} to check for containment.
   * @return Whether the given {@link Location} is within this {@link FunkyChunk}.
   */
  boolean isContained(@NotNull Location location);

  /**
   * Checks whether the given world coordinates can be within this {@link FunkyChunk}.
   *
   * @param x The X coordinate in a world.
   * @param z The Z coordinate in a world.
   * @return Whether the given coordinates can be within this {@link FunkyChunk}.
   */
  boolean areCoordinatesContained(int x, int z);
}
