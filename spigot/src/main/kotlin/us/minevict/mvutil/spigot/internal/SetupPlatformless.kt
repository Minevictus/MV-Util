package us.minevict.mvutil.spigot.internal

import co.aikar.commands.BukkitCommandIssuer
import org.bukkit.scheduler.BukkitRunnable
import us.minevict.mvutil.common.utils.Platformless
import us.minevict.mvutil.spigot.MinevictusUtilsSpigot
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
    }
}