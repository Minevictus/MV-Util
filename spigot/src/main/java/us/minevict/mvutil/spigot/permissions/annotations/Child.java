package us.minevict.mvutil.spigot.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets a child for a permission.
 * <p>
 * This is mandatory for permissions which are supposed to have children.
 *
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Child {
  /**
   * The child's permission node.
   *
   * @return The name of the child's permission node.
   */
  String name();

  /**
   * The value to use for this child.
   * <p>
   * This is not mandatory and defaults to `true`.
   *
   * @return The value of the child.
   */
  boolean value() default true;
}
