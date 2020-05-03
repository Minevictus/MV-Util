package us.minevict.mvutil.common.utils;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.CommandIssuer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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
    Platformless.messageSender = PlatformlessMessageSenderSpigot.INSTANCE;
    Platformless.asyncRunner = new PlatformlessAsyncRunnerSpigot(plugin);
  }

  public static class PlatformlessMessageSenderSpigot implements
      BiConsumer<CommandIssuer, BaseComponent[]> {
    public static final PlatformlessMessageSenderSpigot INSTANCE = new PlatformlessMessageSenderSpigot();

    private PlatformlessMessageSenderSpigot() {
    }

    @Override
    public void accept(CommandIssuer commandIssuer, BaseComponent[] baseComponents) {
      if (baseComponents == null || !(commandIssuer instanceof BukkitCommandIssuer)) {
        return;
      }

      ((BukkitCommandIssuer) commandIssuer).getIssuer().sendMessage(baseComponents);
    }
  }

  public static class PlatformlessAsyncRunnerSpigot implements
      Function<Supplier, CompletableFuture> {
    private final Plugin plugin;

    public PlatformlessAsyncRunnerSpigot(Plugin plugin) {
      this.plugin = plugin;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public CompletableFuture apply(Supplier supplier) {
      CompletableFuture future = new CompletableFuture();
      new BukkitRunnable() {
        @Override
        public void run() {
          try {
            future.complete(supplier.get());
          } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
          }
        }
      }.runTaskAsynchronously(plugin);
      return future;
    }
  }
}
