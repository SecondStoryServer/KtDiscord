package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.Updatable
import me.syari.discord.entity.api.Emoji
import me.syari.discord.entity.api.Server
import me.syari.discord.entity.api.TextChannel
import me.syari.discord.entity.enums.ChannelType
import me.syari.discord.util.json.JsonUtil.getOrNull

class ServerImpl: Server, Updatable {
    private val emojis = mutableMapOf<Long, Emoji>()
    private val roles = mutableMapOf<Long, String>()
    private val textChannels = mutableMapOf<Long, TextChannel>()

    override fun update(json: JsonObject) {
        updateEmoji(json)
        updateRole(json)
        updateChannel(json)
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

    private fun updateChannel(json: JsonObject) {
        val channelObjects = json.getOrNull("channels")?.asJsonArray?.map { it.asJsonObject }
        channelObjects?.forEach {
            when (ChannelType.get(it["type"].asInt)) {
                ChannelType.GUILD_TEXT -> {
                    val channelId = it["id"].asLong
                    val channelName = it["name"].asString
                    textChannels[channelId] = TextChannel(channelName, channelId)
                }
            }
        }
    }
}