package us.minevict.mvutil.bungee.channel;

import net.md_5.bungee.api.connection.Server;
import us.minevict.mvutil.common.channel.IPacketChannel;

/**
 * Extension of {@link IPacketChannel} with Bungee generics.
 *
 * @param <P> The type of packets to flow through this channel and its handler.
 * @since 4.0.0
 */
public interface PacketChannel<P> extends IPacketChannel<P, Server> {
}
