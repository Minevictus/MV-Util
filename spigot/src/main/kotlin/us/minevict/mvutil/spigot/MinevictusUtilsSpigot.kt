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
package us.minevict.mvutil.spigot

import co.aikar.commands.MessageType
import co.aikar.commands.PaperCommandManager
import co.aikar.idb.Database
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.Protocol
import us.minevict.mvutil.common.MinevictusUtilsPlatform
import us.minevict.mvutil.spigot.ext.BukkitChatColor
import us.minevict.mvutil.spigot.ext.copyResource
import us.minevict.mvutil.spigot.ext.createHikariDatabase
import us.minevict.mvutil.spigot.funkychunk.MegaChunk
import us.minevict.mvutil.spigot.internal.SetupPlatformless
import java.io.File
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.logging.Logger

/**
 * @since 5.0.0
 */
class MinevictusUtilsSpigot : JavaPlugin(), MinevictusUtilsPlatform<Plugin, PaperCommandManager> {
    override lateinit var redis: JedisPool
        private set
    override val platformLogger: Logger
        get() = logger

    override fun onLoad() {
        SetupPlatformless.setup(this)
    }

    override fun onEnable() {
        saveDefaultConfig()
        config.options().copyDefaults(true)

        redis = JedisPool(
            GenericObjectPoolConfig<Jedis>(),
            config.getString("redis-hostname"),
            config.getInt("redis-port"),
            Protocol.DEFAULT_TIMEOUT,
            config.getString("redis-password")?.takeUnless(String::isBlank)
        )

        MegaChunk.MEGA_CHUNK_SIZE = config.getInt("megachunk-size")
        MegaChunk.MEGA_CHUNK_OFFSET_X = config.getInt("megachunk-offset-x")
        MegaChunk.MEGA_CHUNK_OFFSET_Z = config.getInt("megachunk-offset-z")

        if (config.getBoolean("megachunk-random-offset-x"))
            MegaChunk.MEGA_CHUNK_OFFSET_X = ThreadLocalRandom.current().nextInt()
        if (config.getBoolean("megachunk-random-offset-z"))
            MegaChunk.MEGA_CHUNK_OFFSET_Z = ThreadLocalRandom.current().nextInt()
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        redis.close()
    }

    override fun prepareAcf(commandManager: PaperCommandManager): PaperCommandManager {
        fun messageType(type: MessageType) {
            for (colour in BukkitChatColor.values()) {
                commandManager.setFormat(type, colour.ordinal, colour)
            }
        }
        messageType(MessageType.ERROR)
        messageType(MessageType.INFO)
        messageType(MessageType.SYNTAX)
        messageType(MessageType.HELP)

        val acfLangFile = File(commandManager.plugin.dataFolder, "acf-lang-en.yml")
        copyResource("acf-lang-en.yml", acfLangFile)

        commandManager.locales.loadYamlLanguageFile(acfLangFile, Locale.ENGLISH)

        return commandManager
    }

    override fun prepareDatabase(
        databaseName: String,
        username: String,
        password: String,
        hostAndPort: String,
        plugin: Plugin
    ): Database {
        return plugin.createHikariDatabase(
            username.ifEmpty { (config.getString("sql-username") ?: throw IllegalArgumentException("user cannot be null")) },
            password.ifEmpty { (config.getString("sql-password") ?: throw IllegalArgumentException("password cannot be null")) },
            (config.getString("sql-db-prefix") ?: "") + databaseName,
            hostAndPort.ifEmpty { (config.getString("sql-host-and-port")
                ?: throw IllegalArgumentException("host and port cannot be null")) }
        )
    }

    companion object {
        /**
         * Gets the current instance of this plugin.
         *
         * @return current instance of this plugin.
         */
        val instance: MinevictusUtilsSpigot
            get() = getPlugin(MinevictusUtilsSpigot::class.java)
    }
}