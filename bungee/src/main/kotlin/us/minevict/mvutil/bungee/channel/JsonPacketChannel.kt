package us.minevict.mvutil.bungee.channel

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.Server
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
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
) : PacketChannel<P>, Listener {
    init {
        plugin.proxy.registerChannel(channel)
        plugin.proxy.pluginManager.registerListener(plugin, this)
    }

    @EventHandler
    fun onPluginMessageReceived(event: PluginMessageEvent) {
        val channel = event.tag
        if (channel != this.channel) return // Incorrect channel found.
        val message = event.data
        if (message.isEmpty()) {
            // No data, null packet received!
            if (!permitNulls) throw IllegalArgumentException("does not permit nulls but attempted null packet")
            handler.packetReceived(null, event.sender, channel)
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
        handler.packetReceived(packet, event.sender, channel)
    }

    override fun sendPacket(packet: P?): Boolean {
        return sendPacket(
            ProxyServer.getInstance().players.firstOrNull()?.server ?: return false,
            packet
        )
    }

    override fun sendPacket(receiver: Server?, packet: P?): Boolean {
        if (!permitNulls && packet == null) throw IllegalArgumentException("does not permit nulls but attempted null packet")
        val realReceiver = receiver
            ?: ProxyServer.getInstance().players.firstOrNull()?.server
            ?: return false

        val handledPacket = handler.packetPreSend(packet, realReceiver)
        if (!permitNulls && handledPacket == null) throw IllegalArgumentException("does not permit nulls but attempted null packet")

        val bytes = if (handledPacket == null) byteArrayOf()
        else simpleGson.toJson(handledPacket).toByteArray()

        // TODO(Proximyst): Support arbitrary message sizes
        if (bytes.size > MAX_MESSAGE_SIZE) {
            throw IllegalArgumentException("max message size exceeded")
        }

        realReceiver.sendData(channel, bytes)
        return true
    }

    override fun unregisterIncoming() {
        plugin.proxy.pluginManager.unregisterListener(this)
    }

    companion object {
        /**
         * The maximum message size of a transmitted packet in bytes.
         */
        const val MAX_MESSAGE_SIZE = 32766
    }
}