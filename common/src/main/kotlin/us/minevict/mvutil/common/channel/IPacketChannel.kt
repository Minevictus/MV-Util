package us.minevict.mvutil.common.channel

/**
 * A plugin messaging channel which uses a specific packet type.
 *
 * @param P The type of packets to flow through this channel and its handler.
 * @param RCV Receiver of the packet.
 * @since 4.0.0
 */
interface IPacketChannel<P, in RCV : Any> {
    /**
     * Whether this channel permits nulls flowing through it.
     *
     * @return Whether this permits nulls.
     */
    val permitNulls: Boolean

    /**
     * Get the type of packet this channel handles.
     *
     * @return The type of packet this handles.
     */
    val packetType: Class<out P>

    /**
     * This channel's name.
     *
     * @return The name of this channel.
     */
    val channel: String

    /**
     * Send a packet through the channel.
     *
     * @param receiver Who should receive this packet.
     * @param packet The packet to send or null if [permitNulls] is true.
     * @return Whether the packet was successfully sent.
     */
    fun sendPacket(receiver: RCV?, packet: P?): Boolean

    /**
     * Send a packet through the channel.
     *
     * @param packet The packet to send or null if [permitNulls] is true.
     * @return Whether the packet was successfully sent.
     */
    fun sendPacket(packet: P?): Boolean

    /**
     * Unregister all incoming packets to this specific [IPacketChannel]. This does not unregister the plugin
     * messaging channel as a whole if this is not the last channel.
     */
    fun unregisterIncoming()
}