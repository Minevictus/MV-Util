package us.minevict.mvutil.spigot.permissions;

import java.util.List;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

class PermissionIndex {
  @NotNull
  private final Plugin plugin;

  @NotNull
  private final Object index;

  @NotNull
  private final List<Permission> ownedPermissions;

  public PermissionIndex(
      @NotNull Plugin plugin,
      @NotNull Object index,
      @NotNull List<Permission> ownedPermissions
  ) {
    this.plugin = plugin;
    this.index = index;
    this.ownedPermissions = ownedPermissions;
  }

  @NotNull
  public Plugin getPlugin() {
    return plugin;
  }

  @NotNull
  public Object getIndex() {
    return index;
  }

  @NotNull
  public List<Permission> getOwnedPermissions() {
    return ownedPermissions;
  }
}
