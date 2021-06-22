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
package us.minevict.mvutil.bungee

import co.aikar.commands.BungeeCommandManager
import co.aikar.commands.MessageType
import co.aikar.idb.Database
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.Protocol
import us.minevict.mvutil.bungee.ext.copyResource
import us.minevict.mvutil.bungee.ext.createHikariDatabase
import us.minevict.mvutil.bungee.ext.readBungeeConfig
import us.minevict.mvutil.common.MinevictusUtilsPlatform
import us.minevict.mvutil.common.ext.BungeeChatColor
import java.io.File
import java.util.*
import java.util.logging.Logger

/**
 * @since 5.0.0
 */
class MinevictusUtilsBungee : Plugin(), MinevictusUtilsPlatform<Plugin, BungeeCommandManager> {
    init {
        instance = this
    }

    override val platformLogger: Logger
        get() = logger

    override lateinit var redis: JedisPool
        private set
    private lateinit var configuration: Configuration

    override fun onLoad() {
        SetupPlatformless.setup(this)
    }

    override fun onEnable() {
        configuration = readBungeeConfig()

        redis = JedisPool(
            GenericObjectPoolConfig<Jedis>(),
            configuration.getString("redis-hostname"),
            configuration.getInt("redis-port"),
            Protocol.DEFAULT_TIMEOUT,
            configuration.getString("redis-password")?.takeUnless(String::isBlank)
        )
    }

    override fun onDisable() {
        proxy.pluginManager.unregisterCommands(this)
        proxy.pluginManager.unregisterListeners(this)

        redis.close()
    }

    override fun prepareAcf(commandManager: BungeeCommandManager): BungeeCommandManager {
        fun messageType(type: MessageType) {
            for (colour in BungeeChatColor.values()) {
                commandManager.setFormat(type, colour.ordinal(), colour)
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

    override fun prepareDatabase(databaseName: String, plugin: Plugin): Database {
        return plugin.createHikariDatabase(
            configuration.getString("sql-username")
                ?: throw IllegalArgumentException("user cannot be null"),
            configuration.getString("sql-password")
                ?: throw IllegalArgumentException("password cannot be null"),
            (configuration.getString("sql-db-prefix") ?: "") + databaseName,
            configuration.getString("sql-host-and-port")
                ?: throw IllegalArgumentException("host and port cannot be null")
        )
    }

    companion object {
        lateinit var instance: MinevictusUtilsBungee
            private set
    }
}