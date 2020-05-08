package us.minevict.mvutil.spigot.permissions;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
 *
 * @since 0.1.0
 */
public class PermissionsManager implements Listener {
  @NotNull
  private static final MethodHandles.Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();

  @NotNull
  private final MinevictusUtilsSpigot main;
  @NotNull
  private final Map<Plugin, PermissionIndex> permissionIndices = new HashMap<>();

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
    var permissionIndex = new PermissionIndex(
        owner,
        index,
        findPermissions(index.getClass(), index, field -> true)
    );
    permissionIndex.getOwnedPermissions().parallelStream()
        .forEach(perm -> {
          Bukkit.getPluginManager().removePermission(perm.getName());
          Bukkit.getPluginManager().addPermission(perm);
        });
    permissionIndices.put(owner, permissionIndex);
  }

  public void registerIndex(@NotNull Plugin owner, @NotNull Class<?> index) {
    Objects.requireNonNull(owner, "the owner plugin for a permissions index cannot be null");
    Objects.requireNonNull(index, "a permissions index type cannot be null");

    main.getLogger().info(
        "Registering permissions index for "
            + owner.getName()
            + " ("
            + index.getSimpleName()
            + ")."
    );
    var permissionIndex = new PermissionIndex(
        owner,
        index,
        null,
        findPermissions(index, null, field -> Modifier.isStatic(field.getModifiers()))
    );
    permissionIndex.getOwnedPermissions().parallelStream()
        .forEach(perm -> {
          Bukkit.getPluginManager().removePermission(perm.getName());
          Bukkit.getPluginManager().addPermission(perm);
        });
    permissionIndices.put(owner, permissionIndex);
  }

  private List<Permission> findPermissions(
      @NotNull Class<?> type,
      @Nullable Object index,
      @Nullable Function<Field, Boolean> fieldValid
  ) {
    @Nullable var annotationPermissions = type.getAnnotation(Permissions.class);
    var indexDefault = annotationPermissions != null
        ? annotationPermissions.permissionDefault().value()
        : PermissionDefault.OP;

    var list = new ArrayList<Permission>();
    for (var field : type.getFields()) {
      if (isFieldInvalid(field) || (fieldValid != null && !fieldValid.apply(field))) {
        continue;
      }

      MethodHandle methodHandle;
      try {
        if (Modifier.isStatic(field.getModifiers())) {
          methodHandle = PUBLIC_LOOKUP.findStaticGetter(type, field.getName(), field.getType());
        } else {
          methodHandle = PUBLIC_LOOKUP.findGetter(type, field.getName(), field.getType());
        }
      } catch (NoSuchFieldException e) {
        throw new IllegalStateException("a field cannot exist and not exist at the same time", e);
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException("the given index cannot have jigsaw rules to hinder access", e);
      }

      var permissionDefault = getFieldDefault(field, indexDefault);
      var description = getFieldDescription(field);
      var children = getFieldChildren(field);

      String permissionName;
      try {
        permissionName = (String) (Modifier.isStatic(field.getModifiers())
            ? methodHandle.invoke()
            : methodHandle.invoke(index));
      } catch (Throwable throwable) {
        main.getLogger().warning("Weird exception thrown on getter!");
        throwable.printStackTrace();
        continue; // Is this even possible?
      }

      var permission = new Permission(permissionName, permissionDefault);
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

  private boolean isFieldInvalid(@NotNull Field field) {
    return Modifier.isTransient(field.getModifiers())
        || !Modifier.isPublic(field.getModifiers())
        || !field.getType().isAssignableFrom(String.class);
  }

  @NotNull
  private PermissionDefault getFieldDefault(@NotNull Field field, @NotNull PermissionDefault indexDefault) {
    var annotationDefault = field.getAnnotation(Default.class);
    return annotationDefault != null
        ? annotationDefault.value()
        : indexDefault;
  }

  @Nullable
  private String getFieldDescription(@NotNull Field field) {
    var annotationDescription = field.getAnnotation(Description.class);
    return annotationDescription != null
        ? annotationDescription.value()
        : null;
  }

  @Nullable
  private Map<String, Boolean> getFieldChildren(@NotNull Field field) {
    var annotationChildren = field.getAnnotationsByType(Child.class);
    return annotationChildren != null
        ? Arrays.stream(annotationChildren).collect(Collectors.toMap(Child::name, Child::value))
        : null;
  }

  @EventHandler
  public void onPluginDisable(PluginDisableEvent event) {
    var index = permissionIndices.remove(event.getPlugin());
    if (index != null) {
      index.getOwnedPermissions()
          .parallelStream()
          .forEach(Bukkit.getPluginManager()::removePermission);
    }
  }
}
