package us.minevict.mvutil.spigot.channel;

import org.bukkit.entity.Player;
import us.minevict.mvutil.common.channel.IPacketChannel;

/**
 * Extension of {@link IPacketChannel} with Bukkit generics.
 *
 * @param <P> The type of packets to flow through this channel and its handler.
 * @since 4.0.0
 */
public interface PacketChannel<P> extends IPacketChannel<P, Player> {
}

