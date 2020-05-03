package us.minevict.mvutil.spigot.text;

import com.proximyst.mvnms.BukkitVersion;
import java.util.Objects;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.minevict.mvutil.common.text.ComponentBuilderV2;
import us.minevict.mvutil.spigot.utils.PluginUtils;

/**
 * Applies a {@link net.md_5.bungee.api.chat.HoverEvent HoverEvent} for an item stack using MV-NMS.
 * <p>
 * This requires MV-NMS (checks {@link PluginUtils#isMvNmsPresent()}) to do anything. If the version is unsupported,
 * nothing is changed. The retention given to {@link #join} is ignored.
 *
 * @since 0.3.5
 */
public final class ItemHoverJoiner implements ComponentBuilderV2.JoinerV2 {
  @NotNull
  private final ItemStack item;

  /**
   * Creates a new joiner for hovering over items.
   *
   * @param item The item to show when hovering if possible.
   */
  public ItemHoverJoiner(@NotNull ItemStack item) {
    Objects.requireNonNull(item, "item to show cannot be null");

    this.item = item;
  }

  @Override
  @NotNull
  public ComponentBuilderV2 join(
      @NotNull ComponentBuilderV2 componentBuilder,
      @Nullable FormatRetention retention
  ) {
    if (!PluginUtils.isMvNmsPresent()) {
      return componentBuilder;
    }

    BukkitVersion.getOptionalVersion()
        .map(BukkitVersion::getNmsItems)
        .ifPresent(items -> componentBuilder.hover(items.hoverItem(getItem())));

    return componentBuilder;
  }

  /**
   * Gets the item which whose hover event is supposed to exist.
   *
   * @return The item to add a hover event for.
   */
  @NotNull
  public ItemStack getItem() {
    return item;
  }
}
