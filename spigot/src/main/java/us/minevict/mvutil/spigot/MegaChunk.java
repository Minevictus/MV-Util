package us.minevict.mvutil.spigot;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * This defines a mega-chunk potentially within a world.
 */
public final class MegaChunk {
  /**
   * The size of a single {@link MegaChunk} in chunks.
   */
  static int MEGA_CHUNK_SIZE = 8;

  /**
   * The shift of a {@link MegaChunk} on the X axis.
   */
  static int MEGA_CHUNK_OFFSET_X = 0;

  /**
   * The shift of a {@link MegaChunk} on the Z axis.
   */
  static int MEGA_CHUNK_OFFSET_Z = 0;

  @Nullable
  private final World world;

  private final int x;
  private final int z;

  /**
   * Constructs a new {@link MegaChunk} from a single {@link Location}.
   *
   * @param location The {@link Location} to get the {@link MegaChunk} of.
   * @return The {@link MegaChunk} a {@link Location} is contained within.
   */
  @NotNull
  public static MegaChunk fromLocation(@NotNull Location location) {
    Objects.requireNonNull(location, "the location cannot be null");

    return new MegaChunk(
        location.getWorld(),
        location.getChunk().getX() / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_X,
        location.getChunk().getZ() / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_Z
    );
  }

  /**
   * Constructs a new {@link MegaChunk} from a single {@link Chunk}.
   *
   * @param chunk The {@link Chunk} to get the {@link MegaChunk} of.
   * @return The {@link MegaChunk} a {@link Chunk} is contained within.
   */
  @NotNull
  public static MegaChunk fromChunk(@NotNull Chunk chunk) {
    Objects.requireNonNull(chunk, "the chunk cannot be null");

    return new MegaChunk(
        chunk.getWorld(),
        chunk.getX() / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_X,
        chunk.getZ() / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_Z
    );
  }

  /**
   * Constructs a new {@link MegaChunk} from coordinates within a potentially existing {@link World world}.
   *
   * @param world The {@link World} to get a {@link MegaChunk} within.
   * @param x     The X coordinate in the world. This is an absolute coordinate.
   * @param z     The Z coordinate in the world. This is an absolute coordinate.
   * @return The {@link MegaChunk} which contains the given coordinates.
   */
  @NotNull
  public static MegaChunk fromCoordinates(@Nullable World world, int x, int z) {
    return new MegaChunk(
        world,
        x / 16 / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_X,
        z / 16 / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_Z
    );
  }

  /**
   * Constructs a new {@link MegaChunk} from coordinates within a potentially existing {@link World world}.
   *
   * @param world The {@link World} to get a {@link MegaChunk} within.
   * @param x     The X coordinate in the world. This is an absolute coordinate.
   * @param z     The Z coordinate in the world. This is an absolute coordinate.
   * @return The {@link MegaChunk} which contains the given coordinates.
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @NotNull
  public static MegaChunk fromCoordinates(@NotNull Optional<World> world, int x, int z) {
    Objects.requireNonNull(world, "the world optional cannot be null!");

    return new MegaChunk(
        world.orElse(null),
        x / 16 / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_X,
        z / 16 / MEGA_CHUNK_SIZE + MEGA_CHUNK_OFFSET_Z
    );
  }

  MegaChunk(@Nullable World world, int x, int z) {
    this.world = world;
    this.x = x;
    this.z = z;
  }

  /**
   * Gets the current size of a single {@link MegaChunk}.
   * <p>
   * The size unit is Minecraft chunks, i.e. 16 x 16 block areas, in each direction from the origin.
   *
   * @return current size of a single {@link MegaChunk}
   */
  public static int getMegaChunkSize() {
    return MEGA_CHUNK_SIZE;
  }

  /**
   * Gets the {@link World} for this {@link MegaChunk}.
   * <p>
   * This is not necessarily available. If one is not available, all methods checking containment will ignore the {@link
   * World}.
   *
   * @return An {@link Optional} wrapped {@link World} for this {@link MegaChunk}, or null.
   */
  @NotNull
  public Optional<World> getWorld() {
    return Optional.ofNullable(world);
  }

  /**
   * Gets the {@link MegaChunk}'s X coordinate.
   * <p>
   * This is not a world coordinate.
   *
   * @return {@link MegaChunk}'s X coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the {@link MegaChunk}'s Z coordinate.
   * <p>
   * This is not a world coordinate.
   *
   * @return {@link MegaChunk}'s Z coordinate.
   */
  public int getZ() {
    return z;
  }

  /**
   * Gets the {@link MegaChunk}'s minimum X coordinate in the world.
   *
   * @return {@link MegaChunk}'s minimum X coordinate in the world.
   */
  public int getMinX() {
    return x * 16;
  }

  /**
   * Gets the {@link MegaChunk}'s minimum Z coordinate in the world.
   *
   * @return {@link MegaChunk}'s minimum Z coordinate in the world.
   */
  public int getMinZ() {
    return z * 16;
  }

  /**
   * Gets the {@link MegaChunk}'s maximum X coordinate in the world.
   *
   * @return {@link MegaChunk}'s maximum X coordinate in the world.
   */
  public int getMaxX() {
    return (x + 1) * 16 - 1;
  }

  /**
   * Gets the {@link MegaChunk}'s maximum Z coordinate in the world.
   *
   * @return {@link MegaChunk}'s maximum Z coordinate in the world.
   */
  public int getMaxZ() {
    return (z + 1) * 16 - 1;
  }

  /**
   * Checks whether the given {@link Location} is/can be within this {@link MegaChunk}.
   *
   * @param location The {@link Location} to check for containment.
   * @return Whether the given {@link Location} is within this {@link MegaChunk}.
   */
  public boolean isContained(@NotNull Location location) {
    Objects.requireNonNull(location, "location cannot be null!");

    // Check that our worlds are the same.
    return location.getWorld() == world

        // X chunk coordinate check is in [x, x + getMegaChunkSize()]
        && location.getChunk().getX() >= getX()
        && location.getChunk().getX() <= (getX() + getMegaChunkSize())

        // Z chunk coordinate check is in [z, z + getMegaChunkSize()]
        && location.getChunk().getZ() >= getZ()
        && location.getChunk().getZ() <= (getZ() + getMegaChunkSize());
  }

  /**
   * Checks whether the given world coordinates can be within this {@link MegaChunk}.
   *
   * @param x The X coordinate in a world.
   * @param z The Z coordinate in a world.
   * @return Whether the given coordinates can be within this {@link MegaChunk}.
   */
  public boolean areCoordinatesContained(int x, int z) {
    return x >= getMinX()
        && x <= getMaxX()

        && z >= getMinZ()
        && z <= getMaxZ();
  }
}
