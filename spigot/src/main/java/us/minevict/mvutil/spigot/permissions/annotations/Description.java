package us.minevict.mvutil.spigot.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the description for a permission.
 * <p>
 * This is not mandatory.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Description {
  /**
   * The description of the permission node.
   */
  String value();
}
