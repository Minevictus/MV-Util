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
 * @deprecated This is not used during parsing and will be removed at a further date.
 */
// Version 0.3.0: Remove this.
@Deprecated
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
