package me.syari.discord.entity.api

import com.google.gson.JsonObject
import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient
import me.syari.discord.util.json.JsonUtil

/**
 * ServerTextChannel
 */
class TextChannel(json: JsonObject) {
    val name: String = json["name"].asString
    val id = json["id"].asLong

    suspend fun send(message: String) {
        RestClient.request(EndPoint.CreateMessage(id), JsonUtil.json {
            "content" to message
        })
    }
}