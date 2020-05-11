package us.minevict.mvutil.bungee

import co.aikar.commands.BungeeCommandManager
import co.aikar.commands.MessageType
import co.aikar.idb.Database
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import us.minevict.mvutil.bungee.ext.copyResource
import us.minevict.mvutil.bungee.ext.createHikariDatabase
import us.minevict.mvutil.bungee.ext.readBungeeConfig
import us.minevict.mvutil.common.MinevictusUtilsPlatform
import us.minevict.mvutil.common.ext.BungeeChatColor
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
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

    override lateinit var redis: RedisClient
        private set
    private lateinit var configuration: Configuration

    override fun onLoad() {
        SetupPlatformless.setup(this)
    }

    override fun onEnable() {
        configuration = readBungeeConfig()

        redis = RedisClient.create(
            RedisURI.builder()
                .withHost(configuration.getString("redis-hostname"))
                .withPort(configuration.getInt("redis-port"))
                .build()
        )
    }

    override fun onDisable() {
        proxy.pluginManager.unregisterCommands(this)
        proxy.pluginManager.unregisterListeners(this)

        redis.shutdown(2, 5, TimeUnit.SECONDS)
    }

    override fun prepareAcf(commandManager: BungeeCommandManager): BungeeCommandManager {
        fun messageType(type: MessageType) {
            for (colour in BungeeChatColor.values()) {
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

    override fun prepareDatabase(databaseName: String, plugin: Plugin): Database {
        return plugin.createHikariDatabase(
            configuration.getString("sql-username")
                ?: throw IllegalArgumentException("user cannot be null"),
            configuration.getString("sql-password")
                ?: throw IllegalArgumentException("password cannot be null"),
            databaseName,
            configuration.getString("sql-host-and-port")
                ?: throw IllegalArgumentException("host and port cannot be null")
        )
    }

    companion object {
        lateinit var instance: MinevictusUtilsBungee
            private set
    }
}