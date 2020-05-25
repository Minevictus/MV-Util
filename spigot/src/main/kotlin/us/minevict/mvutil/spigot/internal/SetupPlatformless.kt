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
package us.minevict.mvutil.spigot.internal

import co.aikar.commands.BukkitCommandIssuer
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import us.minevict.mvutil.common.utils.Platformless
import us.minevict.mvutil.spigot.MinevictusUtilsSpigot
import us.minevict.mvutil.spigot.ext.copyResource
import java.util.concurrent.CompletableFuture

/**
 * @since 5.0.0
 */
internal object SetupPlatformless {
    fun setup(plugin: MinevictusUtilsSpigot) {
        Platformless.messageSender = { issuer, components ->
            if (issuer is BukkitCommandIssuer) {
                issuer.player.sendMessage(*components)
            }
        }
        Platformless.asyncRunner = {
            val future = CompletableFuture<Any?>()
            object : BukkitRunnable() {
                override fun run() {
                    try {
                        future.complete(it())
                    } catch (ex: Throwable) {
                        future.completeExceptionally(ex)
                    }
                }
            }.runTaskAsynchronously(plugin)
            future
        }
        Platformless.exportJarResource = b@{ plug, name, file ->
            if (plug is Plugin) plug.copyResource(name, file)
            else false
        }
    }
}