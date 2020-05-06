package us.minevict.mvutil.bungee.channel;

import java.util.function.BiFunction;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * A handler for a {@link PacketChannel}.
 *
 * @param <P> The type of packets to handle.
 * @since 3.8.0
 */
@FunctionalInterface
public interface PluginMessagePacketHandler<P> {
  /**
   * Handle a received packet.
   *
   * @param packet The packet received; may be null if {@link PacketChannel#permitsNulls()} is true.
   * @param channel The channel name of the {@link PacketChannel}.
   */
  void packetReceived(P packet, @NotNull ProxiedPlayer player, @NotNull String channel);

  /**
   * Handle a packet before it is sent.
   *
   * @param packet The packet to send; may be null if {@link PacketChannel#permitsNulls()} is true.
   * @param player The player whose connection will be used for the packet transmission.
   * @return The packet to send; may be null if {@link PacketChannel#permitsNulls()} is true.
   */
  default P packetPreSend(P packet, @NotNull ProxiedPlayer player) {
    return packet;
  }

  /**
   * Construct a new handler which only handles {@link #packetPreSend}. This means {@link #packetReceived} does nothing
   * at all.
   *
   * @param function The handler for {@link #packetPreSend}.
   * @param <P>      The type of packet to handle.
   * @return The packet handler.
   */
  static <P> PluginMessagePacketHandler<P> onlyPreSend(BiFunction<P, ProxiedPlayer, P> function) {
    return new PluginMessagePacketHandler<>() {
      @Override
      public void packetReceived(P packet, @NotNull ProxiedPlayer player, @NotNull String channel) {
      }

      @Override
      public P packetPreSend(P packet, @NotNull ProxiedPlayer player) {
        return function.apply(packet, player);
      }
    };
  }
}
