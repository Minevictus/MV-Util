package us.minevict.mvutil.common.channel

/**
 * A handler for an [IPacketChannel].
 *
 * @param PKT The type of packets to handle.
 * @param SND The object type that will be used for the packet transmission.
 * @param RCV The object type that is used to receive the packet, it may be the same as {@link SND}.
 * @since 5.0.0
 */
interface IPluginMessagePacketHandler<PKT : Any?, in SND : Any, RCV : Any> {
    /**
     * Handle a received packet.
     *
     * @param packet The packet received; may be null if [IPacketChannel.permitNulls] is true.
     * @param receiver The object whose connection was used for the packet transmission.
     * @param channel The channel name of the [IPacketChannel].
     */
    fun packetReceived(packet: PKT?, receiver: RCV, channel: String)

    /**
     * Handle a packet before it is sent.
     *
     * @param packet The packet to send; may be null if [IPacketChannel.permitNulls] is true.
     * @param sender The object whose connection will be used for the packet transmission.
     * @return The packet to send; may be null if [IPacketChannel.permitNulls] is true.
     */
    fun packetPreSend(packet: PKT?, sender: SND): PKT? = packet

    companion object {
        /**
         * Construct a new handler which only handles [packetPreSend]. This means [packetReceived] does nothing at all.
         *
         * @param block The handler for [packetPreSend].
         * @param PKT The type of packet to handle.
         * @param SND The object type that will be used for the packet transmission.
         * @param RCV The object type that is used to receive the packet, it may be the same as [SND].
         * @return The packet handler.
         */
        fun <PKT, SND : Any, RCV : Any> presend(block: (packet: PKT?, sender: SND) -> PKT?) =
            object : IPluginMessagePacketHandler<PKT, SND, RCV> {
                override fun packetReceived(packet: PKT?, receiver: RCV, channel: String) = Unit
                override fun packetPreSend(packet: PKT?, sender: SND): PKT? = block(packet, sender)
            }

        /**
         * Construct a new handler which only handles [packetReceived]. This means [packetPreSend] is an identity method.
         *
         * @param block The handler for [packetReceived].
         * @param PKT The type of packet to handle.
         * @param SND The object type that will be used for the packet transmission.
         * @param RCV The object type that is used to receive the packet, it may be the same as [SND].
         * @return The packet handler.
         */
        fun <PKT, SND : Any, RCV : Any> recv(block: (packet: PKT?, receiver: RCV, channel: String) -> Unit) =
            object : IPluginMessagePacketHandler<PKT, SND, RCV> {
                override fun packetReceived(packet: PKT?, receiver: RCV, channel: String) =
                    block(packet, receiver, channel)

                override fun packetPreSend(packet: PKT?, sender: SND): PKT? = packet
            }
    }
}