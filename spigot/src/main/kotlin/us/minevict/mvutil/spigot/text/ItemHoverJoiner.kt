/**
 * MV-Util
 * Copyright (C) 2020 Mariell Hoversholm, Nahuel Dolores
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.minevict.mvutil.spigot.text

import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.inventory.ItemStack
import us.minevict.mvutil.spigot.utils.PluginUtils

/**
 * Applies a [HoverEvent] for an item stack using MV-NMS.
 * <p>
 * This requires MV-NMS (checks [PluginUtils.isMvNmsPresent]) to do anything. If the version is unsupported,
 * nothing is changed. The retention given to [join] is ignored.
 *
 * @since 5.0.0
 */
@Deprecated("No longer supported")
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

//        BukkitVersion.getOptionalVersion()
//            .map(BukkitVersion::getNmsItems)
//            .ifPresent { componentBuilder.hover(it.hoverItem(item)) }

        return componentBuilder
    }
}