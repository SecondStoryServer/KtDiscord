package me.syari.discord

import me.syari.discord.entity.api.Message
import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient
import me.syari.discord.websocket.GatewayClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.Delegates

object KtDiscord {
    const val NAME = "KtDiscord"
    const val VERSION = "1.0-dev"
    const val GITHUB_URL = "https://github.com/sya-ri/KtDiscord"
    const val API_VERSION = 6

    val LOGGER: Logger = LoggerFactory.getLogger(NAME)

    init {
        LOGGER.info("$NAME v$VERSION ($GITHUB_URL)")
    }

    internal lateinit var token: String
        private set
    var status = ConnectStatus.DISCONNECTED
        internal set
    var shard by Delegates.notNull<Int>()
        private set
    var maxShards by Delegates.notNull<Int>()
        private set
    internal val gatewayIntents = setOf(GatewayIntent.GUILD_MESSAGES)
    internal lateinit var messageReceiveEvent: (Message) -> Unit

    suspend fun login(token: String, shard: Int = 0, maxShards: Int = 1, messageReceiveEvent: (Message) -> Unit) {
        if (status != ConnectStatus.DISCONNECTED) {
            throw IllegalStateException()
        }
        if (token.isBlank()) {
            throw IllegalArgumentException("")
        }
        this.token = token
        this.shard = shard
        this.maxShards = maxShards

        status = ConnectStatus.CONNECTING

        val gatewayURL = RestClient.request(EndPoint.GetGatewayBot).asJsonObject["url"].asString
        GatewayClient.connect(gatewayURL)

        status = ConnectStatus.CONNECTED

        this.messageReceiveEvent = messageReceiveEvent
    }
}