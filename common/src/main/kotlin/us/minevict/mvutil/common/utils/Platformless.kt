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
package us.minevict.mvutil.common.utils

import co.aikar.commands.CommandIssuer
import net.md_5.bungee.api.chat.BaseComponent
import us.minevict.mvutil.common.IMvPlugin
import java.io.File
import java.util.concurrent.CompletableFuture

object Platformless {
    lateinit var messageSender: (CommandIssuer, Array<BaseComponent>) -> Unit
    lateinit var asyncRunner: ((() -> Any?)) -> CompletableFuture<*>
    lateinit var exportJarResource: (IMvPlugin<*, *, *>, String, File) -> Boolean

    /**
     * Send a message to the [CommandIssuer] regardless of platform.
     *
     * @param issuer The issuer to receive a message.
     * @param components The components to send.
     */
    fun sendMessage(issuer: CommandIssuer, components: Array<BaseComponent>) =
        messageSender(issuer, components)

    /**
     * Run a given block asynchronously.
     *
     * @param block The block to run elsewhere.
     * @param T The type to return in the [CompletableFuture].
     * @return A [CompletableFuture] with the result from the block or its exception if it threw any.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> runAsync(block: () -> T): CompletableFuture<T> =
        asyncRunner(block) as CompletableFuture<T>

    /**
     * Export the given jar resource from the plugin to the file given.
     *
     * @param plugin The plugin whose jar contains the file.
     * @param name The name of the file in the plugin jar.
     * @param destination The destination for the file.
     *
     * @since 5.2.0
     */
    fun exportResource(plugin: IMvPlugin<*, *, *>, name: String, destination: File): Boolean =
        exportJarResource(plugin, name, destination)
}