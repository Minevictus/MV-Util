package us.minevict.mvutil.spigot.permissions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.permissions.PermissionDefault;

/**
 * Used to give common data about the values within a type declaring permissions.
 * <p>
 * This is not mandatory, but is usually preferred.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Permissions {
  /**
   * The default permission grant.
   *
   * @return The default permission grant.
   */
  Default permissionDefault() default @Default(PermissionDefault.OP);
}
