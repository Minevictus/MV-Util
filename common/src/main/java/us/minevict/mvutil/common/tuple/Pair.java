package us.minevict.mvutil.common.tuple;

import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Objects;

/**
 * Stores a tuple of a given {@link A} and {@link B} instances.
 *
 * @param <A> The first type to store.
 * @param <B> The second type to store.
 * @since 0.1.0
 */
public class Pair<A, B> {
  private final A a;
  private final B b;

  /**
   * Constructs a new pair with the given instances of {@link A} and {@link B}.
   *
   * @param a The given {@link A}.
   * @param b The given {@link B}.
   */
  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  /**
   * Gets the current {@link A} instance.
   *
   * @return The current {@link A} instance. This might be null.
   */
  public A getA() {
    return a;
  }

  /**
   * Gets the current {@link B} instance.
   *
   * @return The current {@link B} instance. This might be null.
   */
  public B getB() {
    return b;
  }

  /**
   * Constructs a simple {@link Map.Entry} for this {@link Pair}.
   * <p>
   * {@link A} becomes the type of the key and {@link B} is the value type.
   *
   * @return A mutable {@link Map.Entry} for this {@link Pair}.
   */
  public Map.Entry<A, B> toMapEntry() {
    return new SimpleEntry<>(getA(), getB());
  }

  /**
   * Constructs a simple immutable {@link Map.Entry} for this {@link Pair}.
   * <p>
   * {@link A} becomes the type of the key and {@link B} is the value type.
   *
   * @return An immutable {@link Map.Entry} for this {@link Pair}.
   */
  public Map.Entry<A, B> toImmutableMapEntry() {
    return new SimpleImmutableEntry<>(getA(), getB());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair)) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(getA(), pair.getA()) &&
        Objects.equals(getB(), pair.getB());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getA(), getB());
  }

  @Override
  public String toString() {
    return "Pair{" +
        "a=" + a +
        ", b=" + b +
        '}';
  }
}
