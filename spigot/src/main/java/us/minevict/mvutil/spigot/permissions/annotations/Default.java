package us.minevict.mvutil.spigot.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.permissions.PermissionDefault;

/**
 * Sets the default permission grant for a permission.
 * <p>
 * This is not mandatory. If not present, it inherits the type's default.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Default {
  /**
   * The default permission grant for this permission.
   */
  PermissionDefault value() default PermissionDefault.OP;
}
