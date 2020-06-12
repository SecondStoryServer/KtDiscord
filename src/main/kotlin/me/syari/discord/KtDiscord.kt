package me.syari.discord

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.syari.discord.entity.api.Message
import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient
import me.syari.discord.websocket.GatewayClient
import me.syari.discord.websocket.GatewayIntent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object KtDiscord {
    const val NAME = "KtDiscord"
    const val VERSION = "1.0"
    const val GITHUB_URL = "https://github.com/sya-ri/KtDiscord"
    const val API_VERSION = 6

    internal val LOGGER: Logger = LoggerFactory.getLogger(NAME)

    init {
        LOGGER.info("$NAME v$VERSION ($GITHUB_URL)")
    }

    internal lateinit var token: String
    internal const val shard = 0
    internal const val maxShards = 1
    internal val gatewayIntents = setOf(GatewayIntent.GUILDS, GatewayIntent.GUILD_MESSAGES)
    internal lateinit var messageReceiveEvent: (Message) -> Unit
    internal var status = ConnectStatus.DISCONNECTED

    suspend fun login(token: String, messageReceiveEvent: (Message) -> Unit) {
        if (status != ConnectStatus.DISCONNECTED) {
            throw IllegalStateException()
        }
        if (token.isBlank()) {
            throw IllegalArgumentException("")
        }
        this.token = token

        status = ConnectStatus.CONNECTING

        val gatewayURL = RestClient.request(EndPoint.GetGatewayBot).asJsonObject["url"].asString
        GatewayClient.connect(gatewayURL)

        status = ConnectStatus.CONNECTED

        this.messageReceiveEvent = messageReceiveEvent
    }

    fun loginAsync(token: String, messageReceiveEvent: (Message) -> Unit) {
        GlobalScope.launch {
            login(token, messageReceiveEvent)
        }
    }
}