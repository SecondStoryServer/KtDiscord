package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.api.User
import me.syari.discord.util.json.JsonUtil.getOrNull

class UserImpl(json: JsonObject): User {
    override val name: String = json["username"].asString
    override val id = json["id"].asLong
    override var isBot = json.getOrNull("bot")?.asBoolean ?: false

    override fun toString(): String {
        return "User(name: $name, id: $id, isBot: $isBot)"
    }
}