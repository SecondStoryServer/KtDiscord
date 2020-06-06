package me.syari.discord.entity.api

import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient
import me.syari.discord.util.json.JsonUtil.json

/**
 * ServerTextChannel
 */
data class TextChannel(val name: String, val id: Long) {
    suspend fun send(message: String) {
        RestClient.request(EndPoint.CreateMessage(id), json {
            "content" to message
        })
    }
}