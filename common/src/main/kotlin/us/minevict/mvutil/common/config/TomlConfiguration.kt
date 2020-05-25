package us.minevict.mvutil.common.config

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import us.minevict.mvutil.common.IMvPlugin
import us.minevict.mvutil.common.utils.Platformless
import java.io.File
import java.time.ZoneOffset
import java.util.*

/**
 * A configuration in the TOML language represented by a [TomlKt].
 *
 * @since 5.2.0
 */
@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
class TomlConfiguration<P: IMvPlugin<*, *, *>>(
    private val plugin: P,
    private val file: File,
    private val classPathName: String? = file.name
) {
    lateinit var toml: TomlKt
        private set

    init {
        reload()
    }

    fun reload() {
        // Read resources
        val defaults = Toml()
        if (classPathName != null) {
            // There is a default configuration in the classpath, or so we hope.
            javaClass.getResourceAsStream("/$classPathName")?.use {
                defaults.read(it)
            }
            if (!file.isFile) {
                file.parentFile.mkdirs()
                Platformless.exportResource(plugin, classPathName, file)
            }
        }
        toml = TomlKt(defaults)
        if (file.isFile) {
            toml.read(file)
        }
    }

    fun write() {
        file.writeText(TOML_WRITER.write(toml.toml))
    }

    companion object {
        private val TOML_WRITER = TomlWriter.Builder()
            .padArrayDelimitersBy(1)
            .indentTablesBy(2)
            .timeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
            .build()
    }
}