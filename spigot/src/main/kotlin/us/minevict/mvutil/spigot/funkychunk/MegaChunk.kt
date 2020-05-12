package us.minevict.mvutil.spigot.funkychunk

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World

/**
 * This defines a mega-chunk potentially within a world.
 *
 * @since 0.2.4
 */
class MegaChunk(
    val x: Int,
    val z: Int,
    override val world: World? = null
) : FunkyChunk {
    constructor(location: Location) : this(
        ((location.x / 16).toInt() + MEGA_CHUNK_OFFSET_X) / MEGA_CHUNK_SIZE,
        ((location.z / 16).toInt() + MEGA_CHUNK_OFFSET_Z) / MEGA_CHUNK_SIZE
    )

    constructor(chunk: Chunk) : this(
        (chunk.x + MEGA_CHUNK_OFFSET_X) / MEGA_CHUNK_SIZE,
        (chunk.z + MEGA_CHUNK_OFFSET_Z) / MEGA_CHUNK_SIZE,
        chunk.world
    )

    /**
     * This [MegaChunk]'s minimum X coordinate in the world.
     */
    val minX = x * 16

    /**
     * This [MegaChunk]'s maximum X coordinate in the world.
     */
    val maxX = (x + 1) * 16 - 1

    /**
     * This [MegaChunk]'s minimum Z coordinate in the world.
     */
    val minZ = z * 16

    /**
     * This [MegaChunk]'s maximum Z coordinate in the world.
     */
    val maxZ = (z + 1) * 16 - 1

    override fun isContained(location: Location): Boolean =
        world == location.world && areCoordinatesContained(location.blockX, location.blockZ)

    override fun areCoordinatesContained(x: Int, z: Int): Boolean =
        MegaChunk(x, z).let { it.x == x && it.z == z }

    companion object {
        /**
         * The size of a single mega-chunk in chunks.
         */
        internal var MEGA_CHUNK_SIZE = 8

        /**
         * The shift of a mega-chunk on the X axis.
         */
        internal var MEGA_CHUNK_OFFSET_X = 0

        /**
         * The shift of a mega-chunk on the Z axis.
         */
        internal var MEGA_CHUNK_OFFSET_Z = 0
    }
}