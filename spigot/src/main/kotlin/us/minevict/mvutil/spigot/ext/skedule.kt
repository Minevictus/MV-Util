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