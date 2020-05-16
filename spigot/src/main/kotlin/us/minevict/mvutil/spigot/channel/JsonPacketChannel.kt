package us.minevict.mvutil.spigot.channel

import com.google.gson.JsonSyntaxException
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.Messenger
import org.bukkit.plugin.messaging.PluginMessageListener
import us.minevict.mvutil.common.ext.simpleGson

/**
 * A [PacketChannel] which transmits its packets through serializing as JSON.
 *
 * @param P The type of packets to flow through this channel and its handler.
 * @since 3.8.0
 */
class JsonPacketChannel<P>(
    val plugin: Plugin,
    override val packetType: Class<out P>,
    override val channel: String,
    private val handler: PluginMessagePacketHandler<P>,
    override val permitNulls: Boolean = false
) : PacketChannel<P>, PluginMessageListener {
    init {
        plugin.server.messenger.registerIncomingPluginChannel(plugin, channel, this)
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, channel)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != this.channel) return // Incorrect channel found.
        if (message.isEmpty()) {
            // No data, null packet received!
            if (!permitNulls) throw IllegalArgumentException("does not permit nulls but attempted null packet")
            handler.packetReceived(null, player, channel)
            return
        }

        val string = String(message, Charsets.UTF_8)
        var packet: P? = null
        try {
            packet = simpleGson.fromJson(string, packetType)
        } catch (ex: JsonSyntaxException) {
            plugin.logger.warning("Received malformed packet on plugin messaging channel: $channel")
            plugin.logger.warning("Received packet: $packet")
            ex.printStackTrace()
            return
        }

        // Packet is not null by now.
        handler.packetReceived(packet, player, channel)
    }

    override fun sendPacket(packet: P?): Boolean {
        return sendPacket(
            Bukkit.getOnlinePlayers().firstOrNull() ?: return false,
            packet
        )
    }

    override fun sendPacket(receiver: Player?, packet: P?): Boolean {
        if (!permitNulls && packet == null) throw IllegalArgumentException("does not permit nulls but attempted null packet")
        val realReceiver = receiver
            ?: Bukkit.getOnlinePlayers().firstOrNull()
            ?: return false

        val handledPacket = handler.packetPreSend(packet, realReceiver)
        if (!permitNulls && handledPacket == null) throw IllegalArgumentException("does not permit nulls but attempted null packet")

        val bytes = if (handledPacket == null) byteArrayOf()
        else simpleGson.toJson(handledPacket).toByteArray()

        // TODO(Proximyst): Support arbitrary message sizes
        if (bytes.size > Messenger.MAX_MESSAGE_SIZE) {
            throw IllegalArgumentException("max message size exceeded")
        }

        realReceiver.sendPluginMessage(plugin, channel, bytes)
        return true
    }

    override fun unregisterIncoming() {
        plugin.server.messenger.unregisterIncomingPluginChannel(plugin, channel, this)
    }
}