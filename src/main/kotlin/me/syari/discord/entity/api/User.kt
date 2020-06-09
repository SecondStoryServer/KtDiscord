package me.syari.discord.entity.api

import com.google.gson.JsonObject
import me.syari.discord.util.json.JsonUtil.getOrNull

data class User(val name: String, val id: Long, val isBot: Boolean) {
    companion object {
        fun from(json: JsonObject): User {
            val name: String = json["username"].asString
            val id = json["id"].asLong
            val isBot = json.getOrNull("bot")?.asBoolean ?: false
            return User(name, id, isBot)
        }
    }
}