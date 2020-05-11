package us.minevict.mvutil.spigot.channel

import org.bukkit.entity.Player
import us.minevict.mvutil.common.channel.IPacketChannel
import us.minevict.mvutil.common.channel.IPluginMessagePacketHandler

/**
 * Extension of [IPacketChannel] with Bukkit generics.
 *
 * @param P The type of packets to flow through this channel and its handler.
 * @since 5.0.0
 */
typealias PacketChannel<P> = IPacketChannel<P, Player>

/**
 * Extension of [IPluginMessagePacketHandler] with Bukkit generics.
 *
 * @param P The type of packets to handle.
 * @since 5.0.0
 */
typealias PluginMessagePacketHandler<P> = IPluginMessagePacketHandler<P, Player, Player>