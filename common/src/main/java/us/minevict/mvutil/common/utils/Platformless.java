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
 * Functions which do not require any specific platform while still doing something specific to
 * each.
 *
 * @since 3.6.0
 */
public final class Platformless {
  private Platformless() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  static BiConsumer<CommandIssuer, BaseComponent[]> messageSender = null;
  static Function<Supplier, CompletableFuture> asyncRunner = null;

  public static void sendMessage(@NotNull CommandIssuer issuer,
      @NotNull BaseComponent[] components) {
    messageSender.accept(issuer, components);
  }

  public static void sendMessage(@NotNull CommandIssuer issuer,
      @NotNull ComponentBuilderV2 builder) {
    sendMessage(issuer, builder.create());
  }

  @SuppressWarnings("unchecked")
  public static <T> CompletableFuture<T> runAsync(Supplier<T> runnable) {
    return (CompletableFuture<T>) asyncRunner.apply(runnable);
  }
}
