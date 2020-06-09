package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.entity.api.Emoji
import me.syari.discord.entity.api.Guild
import me.syari.discord.entity.api.Member
import me.syari.discord.entity.api.Message
import me.syari.discord.entity.api.Role
import me.syari.discord.entity.api.TextChannel
import me.syari.discord.entity.api.User
import me.syari.discord.util.json.JsonUtil.getArrayOrNull
import me.syari.discord.util.json.JsonUtil.getOrNull
import java.util.regex.Pattern

object MessageCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        LOGGER.debug("MessageCreateHandler $data")
        handleGuild(data)
    }

    private fun handleGuild(data: JsonObject) {
        val guildId = data.getOrNull("guild_id")?.asLong ?: return
        val guild = Guild.get(guildId) ?: return
        val channelId = data["channel_id"].asLong
        val channel = guild.getTextChannel(channelId) ?: return
        val authorObject = data["author"].asJsonObject
        val author = User.from(authorObject)
        val memberObject = data["member"].asJsonObject
        val member = Member.from(memberObject, author)
        val content = data["content"].asString
        val mentionMembers = getMentionMembers(data)
        val mentionRoles = getMentionRoles(guild, data)
        val mentionChannels = getMentionChannels(guild, content)
        val mentionEmojis = getMentionEmojis(guild, content)
        val message = Message(channel, member, content, mentionMembers, mentionRoles, mentionChannels, mentionEmojis)
        KtDiscord.messageReceiveEvent.invoke(message)
    }

    private fun getMentionMembers(parent: JsonObject): List<Member> {
        val array = parent.getArrayOrNull("mentions")
        return array?.map {
            val data = it.asJsonObject
            val user = User.from(data)
            val memberObject = data["member"].asJsonObject
            Member.from(memberObject, user)
        } ?: emptyList()
    }

    private fun getMentionRoles(guild: Guild, parent: JsonObject): List<Role> {
        return parent.getArrayOrNull("mention_roles")?.mapNotNull {
            guild.getRole(it.asLong)
        } ?: emptyList()
    }

    private fun getMentionChannels(guild: Guild, content: String): List<TextChannel> {
        val pattern = Pattern.compile(TextChannel.REGEX)
        val matcher = pattern.matcher(content)
        return mutableListOf<TextChannel>().apply {
            while (matcher.find()) {
                val channelId = matcher.group(1).toLongOrNull() ?: continue
                val channel = guild.getTextChannel(channelId) ?: continue
                add(channel)
            }
        }
    }

    private fun getMentionEmojis(guild: Guild, content: String): List<Emoji> {
        val pattern = Pattern.compile(Emoji.REGEX)
        val matcher = pattern.matcher(content)
        return mutableListOf<Emoji>().apply {
            while (matcher.find()) {
                val emojiId = matcher.group(2).toLongOrNull() ?: continue
                val emoji = guild.getEmoji(emojiId) ?: continue
                add(emoji)
            }
        }
    }
}