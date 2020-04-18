package us.minevict.mvutil.spigot.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets children for a permission.
 * <p>
 * This is usually set through just using {@link Child @Child}.
 *
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Children {
  /**
   * The children for this permission node.
   *
   * @return All children represented by this annotation.
   */
  Child[] value() default {};
}
