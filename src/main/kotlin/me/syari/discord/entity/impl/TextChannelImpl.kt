package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.api.TextChannel
import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient
import me.syari.discord.util.json.JsonUtil.json

class TextChannelImpl(json: JsonObject): TextChannel {
    override val name: String = json["name"].asString
    override val id = json["id"].asLong

    suspend fun send(message: String) {
        RestClient.request(EndPoint.CreateMessage(id), json {
            "content" to message
        })
    }
}