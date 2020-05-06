package us.minevict.mvutil.spigot.channel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PacketChannel} which transmits its packets through serializing as JSON.
 *
 * @param <P> The type of packets to flow through this channel and its handler.
 * @since 3.8.0
 */
public class JsonPacketChannel<P> implements PacketChannel<P>, PluginMessageListener {
  /**
   * The strict, minimal Gson instance to use for all JSON channels.
   */
  private static final Gson GSON = new Gson();

  /**
   * The plugin which owns this channel.
   */
  @NotNull
  private final Plugin plugin;

  /**
   * The type of packets this channel handles.
   */
  @NotNull
  private final Class<? extends P> packetType;

  /**
   * The channel name of this channel.
   */
  @NotNull
  private final String channel;

  /**
   * The handler for this channel's packets.
   */
  @NotNull
  private final PluginMessagePacketHandler<P> handler;

  /**
   * Whether this channel permits nulls.
   */
  private final boolean permitNulls;

  public JsonPacketChannel(
      @NotNull Plugin plugin,
      @NotNull Class<? extends P> packetType,
      @NotNull String channel,
      @NotNull PluginMessagePacketHandler<P> handler,
      boolean permitNulls
  ) {
    Objects.requireNonNull(plugin, "plugin cannot be null");
    Objects.requireNonNull(packetType, "packetType cannot be null");
    Objects.requireNonNull(channel, "channel cannot be null");
    Objects.requireNonNull(handler, "handler cannot be null");

    this.plugin = plugin;
    this.packetType = packetType;
    this.channel = channel;
    this.handler = handler;
    this.permitNulls = permitNulls;

    plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);
    plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
  }

  public JsonPacketChannel(
      @NotNull Plugin plugin,
      @NotNull Class<? extends P> packetType,
      @NotNull String channel,
      @NotNull PluginMessagePacketHandler<P> handler
  ) {
    this(plugin, packetType, channel, handler, false);
  }

  @Override
  public void onPluginMessageReceived(
      @NotNull String channel,
      @NotNull Player player,
      @NotNull byte[] message
  ) {
    if (!this.channel.equals(channel)) {
      // Not correct channel.
      return;
    }
    if (message.length == 0) {
      // No data, null packet received!
      if (!permitNulls) {
        throw new IllegalArgumentException("does not permit nulls but attempted null packet");
      }
      handler.packetReceived(null, player, channel);
      return;
    }

    var string = new String(message, StandardCharsets.UTF_8);
    P packet;
    try {
      packet = GSON.fromJson(string, packetType);
    } catch (JsonSyntaxException ex) {
      plugin.getLogger().warning("Received malformed packet on plugin messaging channel: " + channel);
      plugin.getLogger().warning("Received packet: " + string);
      ex.printStackTrace();
      return;
    }

    handler.packetReceived(packet, player, channel);
  }

  @Override
  public boolean sendPacket(P packet) {
    if (plugin.getServer().getOnlinePlayers().isEmpty()) {
      return false;
    }
    if (!permitNulls && packet == null) {
      throw new IllegalArgumentException("does not permit nulls but attempted null packet");
    }

    var player = plugin.getServer().getOnlinePlayers().iterator().next();
    packet = handler.packetPreSend(packet, player);
    if (!permitNulls && packet == null) {
      throw new IllegalArgumentException("does not permit nulls but attempted null packet");
    }
    var bytes = packet == null ? new byte[0] : GSON.toJson(packet).getBytes(StandardCharsets.UTF_8);

    // TODO(Proximyst): Support arbitrary message sizes
    if (bytes.length > Messenger.MAX_MESSAGE_SIZE) {
      throw new IllegalArgumentException("max message size exceeded");
    }

    player.sendPluginMessage(plugin, this.channel, bytes);
    return true;
  }

  @NotNull
  @Override
  public String getChannel() {
    return channel;
  }

  @NotNull
  @Override
  public Class<? extends P> getPacketType() {
    return packetType;
  }

  @Override
  public void unregisterIncoming() {
    plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, getChannel(), this);
  }

  @Override
  public boolean permitsNulls() {
    return permitNulls;
  }
}
