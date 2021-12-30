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
package us.minevict.mvutil.spigot.utils

import org.bukkit.Bukkit

/**
 * Utilities regarding plugins this plugin can depend on.
 *
 * @since 0.1.0
 */
object PluginUtils {
    /**
     * Gets whether the MV-NMS plugin is currently present.
     *
     * @return Whether MV-NMS is present and enabled.
     */
    fun isMvNmsPresent(): Boolean =
        Bukkit.getPluginManager().getPlugin("MV-NMS")?.let {
            it.isEnabled && it.name == "MV-NMS"
        } == true
}