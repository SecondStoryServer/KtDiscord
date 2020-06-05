package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.Updatable
import me.syari.discord.entity.api.User
import me.syari.discord.util.json.JsonUtil.getOrNull

class UserImpl(json: JsonObject): User, Updatable {
    override lateinit var name: String
    override var isBot = false

    init {
        update(json)
    }

    override fun update(json: JsonObject) {
        name = json["username"].asString
        json.getOrNull("bot")?.let { isBot = it.asBoolean }
    }
}