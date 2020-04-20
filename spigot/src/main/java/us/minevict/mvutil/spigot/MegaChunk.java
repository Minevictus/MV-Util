package us.minevict.mvutil.spigot;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

/**
 * This defines a mega-chunk potentially within a world.
 *
 * @deprecated Use {@link us.minevict.mvutil.spigot.funkychunk.MegaChunk} instead.
 * @see us.minevict.mvutil.spigot.funkychunk.MegaChunk
 */
// TODO - Version 0.4.0: Remove this.
@Deprecated
public final class MegaChunk extends us.minevict.mvutil.spigot.funkychunk.MegaChunk {
  protected MegaChunk(@Nullable World world, int x, int z) {
    super(world, x, z);
  }
}
