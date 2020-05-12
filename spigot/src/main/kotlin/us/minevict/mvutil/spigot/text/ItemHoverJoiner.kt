package us.minevict.mvutil.spigot.text

import com.proximyst.mvnms.BukkitVersion
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.inventory.ItemStack
import us.minevict.mvutil.common.ext.hover
import us.minevict.mvutil.spigot.utils.PluginUtils

/**
 * Applies a [HoverEvent] for an item stack using MV-NMS.
 * <p>
 * This requires MV-NMS (checks [PluginUtils.isMvNmsPresent]) to do anything. If the version is unsupported,
 * nothing is changed. The retention given to [join] is ignored.
 *
 * @since 5.0.0
 */
class ItemHoverJoiner(
    val item: ItemStack
) : ComponentBuilder.Joiner {
    override fun join(
        componentBuilder: ComponentBuilder,
        retention: ComponentBuilder.FormatRetention?
    ): ComponentBuilder {
        if (!PluginUtils.isMvNmsPresent()) {
            return componentBuilder
        }

        BukkitVersion.getOptionalVersion()
            .map(BukkitVersion::getNmsItems)
            .ifPresent { componentBuilder.hover(it.hoverItem(item)) }

        return componentBuilder
    }
}