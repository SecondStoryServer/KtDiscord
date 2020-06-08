package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.api.Emoji
import me.syari.discord.entity.api.Guild
import me.syari.discord.entity.api.Role
import me.syari.discord.entity.api.TextChannel
import me.syari.discord.entity.api.TextChannel.Companion.allTextChannels
import me.syari.discord.entity.enums.ChannelType
import me.syari.discord.util.json.JsonUtil.getOrNull

class GuildImpl: Guild {
    private val emojis = mutableMapOf<Long, Emoji>()
    private val roles = mutableMapOf<Long, Role>()
    private val textChannels = mutableMapOf<Long, TextChannel>()

    private fun update(json: JsonObject) {
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
            roles[roleId] = Role(roleName, roleId)
        }
    }

    private fun updateChannel(json: JsonObject) {
        val channelObjects = json.getOrNull("channels")?.asJsonArray?.map { it.asJsonObject }
        channelObjects?.forEach {
            when (ChannelType.get(it["type"].asInt)) {
                ChannelType.GUILD_TEXT -> {
                    val channelId = it["id"].asLong
                    val channelName = it["name"].asString
                    val channel = TextChannel(channelName, channelId)
                    textChannels[channelId] = channel
                    allTextChannels[channelId] = channel
                }
            }
        }
    }

    override fun getTextChannel(id: Long): TextChannel? {
        return textChannels[id]
    }

    override fun getRole(id: Long): Role? {
        return roles[id]
    }

    companion object {
        private val serverList = mutableMapOf<Long, GuildImpl>()

        internal val allServer
            get() = serverList.values.toSet()

        internal fun get(id: Long): GuildImpl? {
            return serverList[id]
        }

        internal fun putOrUpdate(id: Long, json: JsonObject) {
            serverList.getOrPut(id) { GuildImpl() }.update(json)
        }
    }
}