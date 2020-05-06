package us.minevict.mvutil.bungee.channel;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.jetbrains.annotations.NotNull;

/**
 * A plugin messaging channel which uses a specific packet type.
 *
 * @param <P> The type of packets to flow through this channel and its handler.
 * @since 3.8.0
 */
public interface PacketChannel<P> {
  /**
   * Send a packet through the channel.
   *
   * @param packet The packet to send or null if {@link #permitsNulls()} is true.
   * @param player The player whose server should receive this packet.
   * @return Whether a packet was successfully sent.
   */
  default boolean sendPacket(@NotNull ProxiedPlayer player, P packet) {
    return sendPacket(player.getServer(), packet);
  }

  /**
   * Send a packet through the channel.
   *
   * @param packet The packet to send or null if {@link #permitsNulls()} is true.
   * @param server The server to receive this packet.
   * @return Whether a packet was successfully sent.
   */
  boolean sendPacket(@NotNull Server server, P packet);

  /**
   * Get the type of the packet this channel handles.
   *
   * @return The type of packet this handles.
   */
  @NotNull
  Class<? extends P> getPacketType();

  /**
   * This channel's name.
   *
   * @return The name of this channel.
   */
  @NotNull
  String getChannel();

  /**
   * Unregister all incoming packets to this specific {@link PacketChannel}. This does not unregister the plugin
   * messaging channel as a whole if this is not the last channel.
   */
  void unregisterIncoming();

  /**
   * Whether this channel permits nulls flowing through it.
   *
   * @return Whether this permits nulls.
   */
  boolean permitsNulls();
}
