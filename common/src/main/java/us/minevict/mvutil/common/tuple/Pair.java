package us.minevict.mvutil.common.tuple;

import java.util.Objects;

public class Pair<A, B> {
  private final A a;
  private final B b;

  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public A getA() {
    return a;
  }

  public B getB() {
    return b;
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