package us.minevict.mvutil.spigot.channel;

import org.bukkit.entity.Player;
import us.minevict.mvutil.common.channel.IPluginMessagePacketHandler;

/**
 * A Spigot handler for a {@link PacketChannel}.
 *
 * @param <P> The type of packets to handle.
 * @since 3.8.0
 */
public interface PluginMessagePacketHandler<P>
    extends IPluginMessagePacketHandler<P, Player, Player> {
}
