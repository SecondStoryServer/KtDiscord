package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.Updatable
import me.syari.discord.entity.api.Emoji
import me.syari.discord.entity.api.Server

class ServerImpl: Server, Updatable {
    private val emojis = mutableMapOf<Long, Emoji>()
    private val roles = mutableMapOf<Long, String>()

    override fun update(json: JsonObject) {
        updateEmoji(json)
        updateRole(json)
    }

    private fun updateEmoji(json: JsonObject) {
        val emojiObjects = json["emojis"].asJsonArray.map { it.asJsonObject }
        emojiObjects.forEach {
            val emojiName = it["name"].asString
            val emojiId = it["id"].asLong
            val isAnimated = it["animated"].asBoolean
            emojis[emojiId] = Emoji(emojiName, emojiId, isAnimated)
        }
    }

    private fun updateRole(json: JsonObject) {
        val roleObjects = json["roles"].asJsonArray.map { it.asJsonObject }
        roleObjects.forEach {
            val roleName = it["name"].asString
            val roleId = it["id"].asLong
            roles[roleId] = roleName
        }
    }
}