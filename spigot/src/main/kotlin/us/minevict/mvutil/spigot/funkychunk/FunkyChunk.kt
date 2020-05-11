package us.minevict.mvutil.spigot.funkychunk

import org.bukkit.Location
import org.bukkit.World

/**
 * This defines a funky-chunk potentially within a world.
 *
 * @since 5.0.0
 */
interface FunkyChunk {
    /**
     * Gets the [World] for this [FunkyChunk].
     *
     * This is not necessarily available. If one is not available, all methods checking containment will ignore the [World].
     *
     * @return The [World] for this [FunkyChunk], or null.
     */
    val world: World?

    /**
     * Checks whether the given [Location] is/can be within this [FunkyChunk].
     *
     * @param location The [Location] to check for containment.
     * @return Whether the given [Location] is within this [FunkyChunk].
     */
    fun isContained(location: Location): Boolean

    /**
     * Checks whether the given world coordinates can be within this [FunkyChunk].
     *
     * @param x The X coordinate in a world.
     * @param z The Z coordinate in a world.
     * @return Whether the given coordinates can be within this [FunkyChunk].
     */
    fun areCoordinatesContained(x: Int, z: Int): Boolean
}