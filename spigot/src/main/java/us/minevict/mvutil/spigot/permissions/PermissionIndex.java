package us.minevict.mvutil.spigot.permissions;

import java.util.List;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.1.0
 */
class PermissionIndex {
  @NotNull
  private final Plugin plugin;

  @NotNull
  private final Class<?> type;

  @Nullable
  private final Object index;

  @NotNull
  private final List<Permission> ownedPermissions;

  public PermissionIndex(
      @NotNull Plugin plugin,
      @NotNull Class<?> type,
      @Nullable Object index,
      @NotNull List<Permission> ownedPermissions
  ) {
    this.plugin = plugin;
    this.type = type;
    this.index = index;
    this.ownedPermissions = ownedPermissions;
  }

  public PermissionIndex(
      @NotNull Plugin plugin,
      @NotNull Object index,
      @NotNull List<Permission> ownedPermissions
  ) {
    this(plugin, index.getClass(), index, ownedPermissions);
  }

  @NotNull
  public Plugin getPlugin() {
    return plugin;
  }

  @NotNull
  public Class<?> getType() {
    return type;
  }

  @Nullable
  public Object getIndex() {
    return index;
  }

  @NotNull
  public List<Permission> getOwnedPermissions() {
    return ownedPermissions;
  }
}
