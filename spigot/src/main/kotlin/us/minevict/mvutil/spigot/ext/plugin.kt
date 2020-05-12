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
 * @param failSafe Whether to retunr false if anything goes wrong during the copy.
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
    failSafe: Boolean = false
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
            if (failSafe) return false
            throw IllegalArgumentException("no filename in classpath: $name")
        }
    } catch (ex: IOException) {
        if (failSafe) return false
        throw ex
    }

    return true
}