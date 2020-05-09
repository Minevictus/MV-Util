package us.minevict.mvutil.common;

import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * A lazily constructed value.
 *
 * @param <T> The type of value to store and create.
 * @since 3.4.0
 */
public final class LazyValue<T> {
  private final Object lock = new Object();
  @NotNull
  private final Supplier<T> constructor;
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Support null values
  private Optional<T> value = Optional.empty();

  /**
   * Constructs a new lazily constructed value using the supplied constructor.
   *
   * @param constructor The constructor to use.
   */
  public LazyValue(@NotNull Supplier<T> constructor) {
    this.constructor = constructor;
  }

  /**
   * Constructs a new value using the supplied value.
   * <p>
   * This does not construct anything.
   *
   * @param value The value to put in this wrapper.
   */
  public LazyValue(T value) {
    this.value = Optional.of(value);
    //noinspection ConstantConditions - special case, it won't be used.
    this.constructor = null;
  }

  /**
   * Checks whether the value is initialised yet.
   *
   * @return Whether an initialised value is stored.
   */
  public boolean isInitialised() {
    return value.isPresent();
  }

  /**
   * Gets the value this holds.
   * <p>
   * If no value is present, one is constructed using the previously supplied constructor.
   *
   * @return The value of type {@link T} this holds.
   */
  public T getValue() {
    if (value.isEmpty()) {
      synchronized (lock) {
        if (value.isEmpty()) {
          T constructed = constructor.get();
          value = Optional.ofNullable(constructed);
          return constructed;
        }
      }
    }

    return value.get();
  }

  /**
   * Gets the value this holds if it is present.
   *
   * @return The value of type {@link T} this holds.
   */
  public Optional<T> getIfInitialised() {
    return value;
  }
}
