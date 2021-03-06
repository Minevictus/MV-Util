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
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Copy a resource from the plugin's classpath to the destination file.
 *
 * @param name Name of the file in the classpath; this is a path such as `configs/test.yml`.
 * @param destination The destination [File].
 * @param overwrite Whether to overwrite the destination if it already exists.
 * @param failSafe Whether to return false if anything goes wrong during the copy.
 * @param generate Whether to generate the config file if it does not exist.
 * @throws IOException Thrown if an error happens while copying the resource and failsafe is false.
 * @throws IllegalStateException Thrown if there is a directory at the destination regardless of failsafe.
 * @throws IllegalArgumentException Thrown if there is no resource under the name given and failsafe is false.
 * @since 5.0.0
 */
@Throws(IOException::class, IllegalStateException::class, IllegalArgumentException::class)
fun Plugin.copyResource(
    name: String,
    destination: File,
    overwrite: Boolean = false,
    failSafe: Boolean = false,
    generate: Boolean = false
): Boolean {
    destination.parentFile.mkdirs()

    if (!overwrite && destination.isFile) return false
    if (destination.isDirectory) {
        // Interesting - let's fail here.
        throw IllegalStateException("there is already a directory at ${destination.absolutePath}")
    }

    try {
        getResource(name)?.use {
            Files.copy(
                it,
                destination.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
        } ?: run {
            if (generate) return File(destination, name).createNewFile()
            if (failSafe) return false
            throw IllegalArgumentException("no filename in classpath: $name")
        }
    } catch (ex: IOException) {
        if (failSafe) return false
        throw ex
    }

    return true
}