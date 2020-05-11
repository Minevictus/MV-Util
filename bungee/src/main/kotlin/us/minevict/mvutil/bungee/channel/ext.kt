package us.minevict.mvutil.bungee.channel

import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.Server
import us.minevict.mvutil.common.channel.IPacketChannel
import us.minevict.mvutil.common.channel.IPluginMessagePacketHandler

/**
 * Extension of [IPacketChannel] with Bungee generics.
 *
 * @param P The type of packets to flow through this channel and its handler.
 * @since 5.0.0
 */
typealias PacketChannel<P> = IPacketChannel<P, Server>

/**
 * Extension of [IPluginMessagePacketHandler] with Bungee generics.
 *
 * @param P The type of packets to handle.
 * @since 5.0.0
 */
typealias PluginMessagePacketHandler<P> = IPluginMessagePacketHandler<P, Server, Connection>