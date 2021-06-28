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
package us.minevict.mvutil.bungee

import co.aikar.commands.BungeeCommandIssuer
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import us.minevict.mvutil.bungee.ext.copyResource
import us.minevict.mvutil.common.utils.Platformless
import java.util.concurrent.CompletableFuture

/**
 * @since 5.0.0
 */
internal object SetupPlatformless {
    fun setup(plugin: MinevictusUtilsBungee) {
        Platformless.messageSender = { issuer, components ->
            if (issuer is BungeeCommandIssuer) {
                issuer.issuer.sendMessage(*components)
            }
        }
        Platformless.asyncRunner = {
            val future = CompletableFuture<Any?>()
            ProxyServer.getInstance().scheduler.runAsync(plugin) {
                try {
                    future.complete(it())
                } catch (ex: Throwable) {
                    future.completeExceptionally(ex)
                }
            }
            future
        }
        Platformless.exportJarResource = b@{ plug, name, file, generate ->
            if (plug is Plugin) plug.copyResource(name, file, failSafe = true, generate = generate)
            else false
        }
    }
}