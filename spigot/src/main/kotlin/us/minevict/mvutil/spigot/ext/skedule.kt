package us.minevict.mvutil.spigot.ext

import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.CoroutineTask
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import org.bukkit.plugin.Plugin

/**
 * Schedule a task to run for the given plugin with a-/synchronous context.
 *
 * @param async Whether this should be asynchronous to start.
 * @param block The block to run upon scheduling.
 * @return The scheduled coroutine task.
 */
fun Plugin.skedule(async: Boolean = true, block: suspend BukkitSchedulerController.() -> Unit): CoroutineTask =
    server.scheduler.schedule(this, if (async) SynchronizationContext.ASYNC else SynchronizationContext.SYNC, block)

/**
 * Switch the current task to continue asynchronously.
 */
suspend fun BukkitSchedulerController.async() = switchContext(SynchronizationContext.ASYNC)

/**
 * Switch the current task to continue synchronously.
 */
suspend fun BukkitSchedulerController.sync() = switchContext(SynchronizationContext.SYNC)