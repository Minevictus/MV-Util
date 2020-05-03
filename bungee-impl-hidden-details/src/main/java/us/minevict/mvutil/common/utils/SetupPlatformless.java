package us.minevict.mvutil.common.utils;

import co.aikar.commands.BungeeCommandIssuer;
import co.aikar.commands.CommandIssuer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @since 3.6.0
 */
public class SetupPlatformless {
  private SetupPlatformless() {
  }

  /**
   * Set up the {@link Platformless} functionality.
   * <p>
   * This <b>must</b> be called (at least) once.
   * </p>
   *
   * @param plugin The plugin executing this.
   */
  public static void setup(Plugin plugin) {
    Platformless.messageSender = PlatformlessMessageSenderBungee.INSTANCE;
    Platformless.asyncRunner = new PlatformlessAsyncRunnerBungee(plugin);
  }

  public static class PlatformlessMessageSenderBungee implements
      BiConsumer<CommandIssuer, BaseComponent[]> {
    public static final PlatformlessMessageSenderBungee INSTANCE = new PlatformlessMessageSenderBungee();

    private PlatformlessMessageSenderBungee() {
    }

    @Override
    public void accept(CommandIssuer commandIssuer, BaseComponent[] baseComponents) {
      if (baseComponents == null || !(commandIssuer instanceof BungeeCommandIssuer)) {
        return;
      }

      ((BungeeCommandIssuer) commandIssuer).getIssuer().sendMessage(baseComponents);
    }
  }

  public static class PlatformlessAsyncRunnerBungee implements
      Function<Supplier, CompletableFuture> {
    private final Plugin plugin;

    public PlatformlessAsyncRunnerBungee(Plugin plugin) {
      this.plugin = plugin;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public CompletableFuture apply(Supplier supplier) {
      CompletableFuture future = new CompletableFuture();
      ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
        try {
          future.complete(supplier.get());
        } catch (Throwable throwable) {
          future.completeExceptionally(throwable);
        }
      });
      return future;
    }
  }
}
