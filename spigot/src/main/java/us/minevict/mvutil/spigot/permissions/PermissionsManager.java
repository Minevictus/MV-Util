package us.minevict.mvutil.spigot.permissions;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.minevict.mvutil.spigot.MinevictusUtilsSpigot;
import us.minevict.mvutil.spigot.permissions.annotations.Child;
import us.minevict.mvutil.spigot.permissions.annotations.Default;
import us.minevict.mvutil.spigot.permissions.annotations.Description;
import us.minevict.mvutil.spigot.permissions.annotations.Permissions;

/**
 * A manager for handling other plugins' permissions needs.
 */
public class PermissionsManager implements Listener {
  @NotNull private static final MethodHandles.Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();

  @NotNull private final MinevictusUtilsSpigot main;
  @NotNull private final Map<Plugin, PermissionIndex> permissionIndices = new HashMap<>();

  public PermissionsManager(@NotNull final MinevictusUtilsSpigot main) {
    this.main = main;
  }

  public void registerIndex(@NotNull Plugin owner, @NotNull Object index) {
    Objects.requireNonNull(owner, "the owner plugin for a permissions index cannot be null");
    Objects.requireNonNull(index, "a permissions index cannot be null");

    main.getLogger().info(
        "Registering permissions index for "
            + owner.getName()
            + " ("
            + index.getClass().getSimpleName()
            + ")."
    );
    PermissionIndex permissionIndex = new PermissionIndex(
        owner,
        index,
        findPermissions(index)
    );
    permissionIndex.getOwnedPermissions().parallelStream()
        .forEach(perm -> {
          Bukkit.getPluginManager().removePermission(perm.getName());
          Bukkit.getPluginManager().addPermission(perm);
        });
    permissionIndices.put(owner, permissionIndex);
  }

  private List<Permission> findPermissions(@NotNull Object index) {
    @Nullable Permissions annotationPermissions = index.getClass().getAnnotation(Permissions.class);
    PermissionDefault indexDefault = annotationPermissions != null
        ? annotationPermissions.permissionDefault().value()
        : PermissionDefault.OP;

    List<Permission> list = new ArrayList<>();
    for (Field field : index.getClass().getFields()) {
      if (Modifier.isTransient(field.getModifiers())
          || !Modifier.isPublic(field.getModifiers())
          || !field.getType().isAssignableFrom(String.class)) {
        continue;
      }

      VarHandle varHandle;
      try {
        if (Modifier.isStatic(field.getModifiers())) {
          varHandle = PUBLIC_LOOKUP.findStaticVarHandle(index.getClass(), field.getName(), field.getType());
        } else {
          varHandle = PUBLIC_LOOKUP.findVarHandle(index.getClass(), field.getName(), field.getType());
        }
      } catch (NoSuchFieldException e) {
        throw new IllegalStateException("a field cannot exist and not exist at the same time", e);
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException("the given index cannot have jigsaw rules to hinder access", e);
      }
      if (!varHandle.varType().isAssignableFrom(String.class)) {
        continue;
      }

      @Nullable Default annotationDefault = field.getAnnotation(Default.class);
      PermissionDefault permissionDefault = annotationDefault != null
          ? annotationDefault.value()
          : indexDefault;

      @Nullable Description annotationDescription = field.getAnnotation(Description.class);
      @Nullable String description = annotationDescription != null
          ? annotationDescription.value()
          : null;

      @Nullable Child[] annotationChildren = field.getAnnotationsByType(Child.class);
      Map<String, Boolean> children = annotationChildren != null
          ? Arrays.stream(annotationChildren).collect(Collectors.toMap(Child::name, Child::value))
          : null;

      String permissionName = (String) varHandle.get(index);

      Permission permission = new Permission(permissionName, permissionDefault);
      if (description != null) {
        permission.setDescription(description);
      }
      if (children != null) {
        permission.getChildren().putAll(children);
      }

      list.add(permission);
    }

    return list;
  }

  @EventHandler
  public void onPluginDisable(PluginDisableEvent event) {
    PermissionIndex index = permissionIndices.remove(event.getPlugin());
    if (index != null) {
      index.getOwnedPermissions()
          .parallelStream()
          .forEach(Bukkit.getPluginManager()::removePermission);
    }
  }
}
