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
            plugin.javaClass.getResourceAsStream("/$classPathName")?.use {
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