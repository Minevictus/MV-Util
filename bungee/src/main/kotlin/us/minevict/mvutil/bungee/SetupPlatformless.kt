package us.minevict.mvutil.bungee

import co.aikar.commands.BungeeCommandIssuer
import net.md_5.bungee.api.ProxyServer
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
    }
}