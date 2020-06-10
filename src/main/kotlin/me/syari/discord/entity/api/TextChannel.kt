package me.syari.discord.entity.api

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.syari.discord.entity.Mentionable
import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient
import me.syari.discord.util.json.JsonUtil.json

/**
 * GuildTextChannel
 */
data class TextChannel internal constructor(val name: String, val id: Long): Mentionable {
    fun send(message: String) {
        GlobalScope.launch {
            RestClient.request(EndPoint.CreateMessage(id), json {
                "content" to message
            })
        }
    }

    override val asMentionDisplay: String
        get() = "#$name"

    override val asMentionRegex: Regex
        get() = "<#$id>".toRegex()

    companion object {
        internal val allTextChannels = mutableMapOf<Long, TextChannel>()

        fun get(id: Long): TextChannel? {
            return allTextChannels[id]
        }

        internal const val REGEX = "<#(\\d+)>"
    }
}