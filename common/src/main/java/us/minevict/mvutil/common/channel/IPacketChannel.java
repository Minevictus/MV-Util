package us.minevict.mvutil.common.channel;

import org.jetbrains.annotations.NotNull;

/**
 * A plugin messaging channel which uses a specific packet type.
 *
 * @param <P>   The type of packets to flow through this channel and its handler.
 * @param <RCV> Receiver of the packet.
 * @since 4.0.0
 */
public interface IPacketChannel<P, RCV> {
  /**
   * Send a packet through the channel.
   *
   * @param receiver Who should receive this packet.
   * @param packet   The packet to send or null if {@link #permitsNulls()} is true.
   * @return Whether a packet was successfully sent.
   */
  boolean sendPacket(@NotNull RCV receiver, P packet);

  /**
   * Send a packet through the channel.
   *
   * @param packet The packet to send or null if {@link #permitsNulls()} is true.
   * @return Whether a packet was successfully sent.
   */
  boolean sendPacket(P packet);

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
   * Unregister all incoming packets to this specific {@link IPacketChannel}. This does not unregister the plugin
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
