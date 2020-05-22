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
package us.minevict.mvutil.spigot.permissions

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

class PermissionsDSL(
    private val permissions: MutableMap<String, Permission> = mutableMapOf()
) {
    fun register(
        node: String,
        default: PermissionDefault? = null,
        description: String? = null,
        children: MutableMap<String, Boolean>? = null,
        block: PermissionConfigurationDSL.() -> Unit = {}
    ) {
        permissions[node] = (Bukkit.getPluginManager().getPermission(node) ?: Permission(node))
            .also { permission ->
                val perm = PermissionConfigurationDSL(
                    this,
                    permission,
                    default,
                    description,
                    children ?: mutableMapOf()
                ).apply(block)
                // Ensure children exist.
                perm.children.keys.forEach {
                    if (!permissions.containsKey(it)
                        && Bukkit.getServer().pluginManager.getPermission(it) == null
                    ) {
                        register(it)
                    }
                }
                perm.default?.also {
                    permission.default = it
                }
                perm.description?.also(permission::setDescription)

                // Register this permission if it does not exist.
                // If it does exist, we've just modified an existing one which should've recalculated all permissibles.
                if (Bukkit.getServer().pluginManager.getPermission(permission.name) == null)
                    Bukkit.getPluginManager().addPermission(permission)
            }
    }

    class PermissionConfigurationDSL(
        private val dsl: PermissionsDSL,
        private val permission: Permission,

        var default: PermissionDefault?,
        var description: String?,
        internal var children: MutableMap<String, Boolean>
    ) {
        fun child(node: String, value: Boolean = true) {
            children[node] = value
        }
    }
}