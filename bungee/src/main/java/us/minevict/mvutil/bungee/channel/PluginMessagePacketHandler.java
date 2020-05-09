package us.minevict.mvutil.bungee.channel;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.Server;
import us.minevict.mvutil.common.channel.IPluginMessagePacketHandler;

/**
 * A handler for a {@link PacketChannel}.
 *
 * @param <P> The type of packets to handle.
 * @since 3.8.0
 */
@FunctionalInterface
public interface PluginMessagePacketHandler<P>
    extends IPluginMessagePacketHandler<P, Server, Connection> {
}
