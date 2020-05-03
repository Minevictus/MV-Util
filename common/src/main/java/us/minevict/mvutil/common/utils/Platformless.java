package us.minevict.mvutil.common.utils;

import co.aikar.commands.CommandIssuer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.text.ComponentBuilderV2;

/**
 * Functions which do not require any specific platform while still doing something specific to each.
 *
 * @since 3.6.0
 */
public final class Platformless {
  private Platformless() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  static BiConsumer<CommandIssuer, BaseComponent[]> messageSender = null;
  static Function<Supplier, CompletableFuture> asyncRunner = null;

  /**
   * Send a message to the {@link CommandIssuer} regardless of platform.
   *
   * @param issuer     The issuer to receive a message.
   * @param components The components to send.
   */
  public static void sendMessage(@NotNull CommandIssuer issuer,
      @NotNull BaseComponent[] components) {
    messageSender.accept(issuer, components);
  }

  /**
   * Send a message to the {@link CommandIssuer} regardless of platform.
   *
   * @param issuer  The issuer to receive a message.
   * @param builder The {@link ComponentBuilderV2} to send.
   */
  public static void sendMessage(@NotNull CommandIssuer issuer,
      @NotNull ComponentBuilderV2 builder) {
    sendMessage(issuer, builder.create());
  }

  /**
   * Run a given {@link Supplier} asynchronously.
   *
   * @param runnable The {@link Supplier} to run elsewhere.
   * @param <T>      The type to return in the {@link CompletableFuture}.
   * @return A {@link CompletableFuture} with the result from the runnable or its exception if it threw any.
   */
  @SuppressWarnings("unchecked")
  public static <T> CompletableFuture<T> runAsync(Supplier<T> runnable) {
    return (CompletableFuture<T>) asyncRunner.apply(runnable);
  }
}
