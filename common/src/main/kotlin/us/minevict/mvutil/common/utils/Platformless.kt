package us.minevict.mvutil.common.utils

import co.aikar.commands.CommandIssuer
import net.md_5.bungee.api.chat.BaseComponent
import java.util.concurrent.CompletableFuture

object Platformless {
    lateinit var messageSender: (CommandIssuer, Array<BaseComponent>) -> Unit
    lateinit var asyncRunner: ((() -> Any?)) -> CompletableFuture<*>

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
}