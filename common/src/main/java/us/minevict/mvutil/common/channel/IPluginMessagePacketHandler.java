package us.minevict.mvutil.common.channel;

import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

/**
 * A handler for a {@link IPacketChannel}.
 *
 * @param <PKT> The type of packets to handle.
 * @param <SND> The object type that will be used for the packet transmission.
 * @param <RCV> The object type that is used to receive the packet, it may be the same as {@link SND}.
 * @since 4.0.0
 */
@FunctionalInterface
public interface IPluginMessagePacketHandler<PKT, SND, RCV> {
  /**
   * Construct a new handler which only handles {@link #packetPreSend}. This means {@link #packetReceived} does nothing
   * at all.
   *
   * @param function The handler for {@link #packetPreSend}.
   * @param <PKT>    The type of packet to handle.
   * @param <SND>    The object type that will be used for the packet transmission.
   * @param <RCV>    The object type that is used to receive the packet, it may be the same as {@link SND}.
   * @return The packet handler.
   */
  static <PKT, SND, RCV> IPluginMessagePacketHandler<PKT, SND, RCV> onlyPreSend(BiFunction<PKT, SND, PKT> function) {
    return new IPluginMessagePacketHandler<>() {
      @Override
      public void packetReceived(PKT packet, @NotNull RCV receiver, @NotNull String channel) {
      }

      @Override
      public PKT packetPreSend(PKT packet, @NotNull SND sender) {
        return function.apply(packet, sender);
      }
    };
  }

  /**
   * Handle a received packet.
   *
   * @param packet   The packet received; may be null if {@link IPacketChannel#permitsNulls()} is true.
   * @param receiver The object whose connection was used for the packet transmission.
   * @param channel  The channel name of the {@link IPacketChannel}.
   */
  void packetReceived(PKT packet, @NotNull RCV receiver, @NotNull String channel);

  /**
   * Handle a packet before it is sent.
   *
   * @param packet The packet to send; may be null if {@link IPacketChannel#permitsNulls()} is true.
   * @param sender The object whose connection will be used for the packet transmission.
   * @return The packet to send; may be null if {@link IPacketChannel#permitsNulls()} is true.
   */
  default PKT packetPreSend(PKT packet, @NotNull SND sender) {
    return packet;
  }
}
