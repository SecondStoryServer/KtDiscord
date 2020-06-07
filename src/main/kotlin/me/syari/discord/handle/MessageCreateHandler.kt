package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.entity.api.Member
import me.syari.discord.entity.api.Message
import me.syari.discord.entity.impl.GuildImpl
import me.syari.discord.entity.impl.MemberImpl
import me.syari.discord.entity.impl.UserImpl
import me.syari.discord.util.json.JsonUtil.getArrayOrNull
import me.syari.discord.util.json.JsonUtil.getOrNull

object MessageCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        LOGGER.debug("MessageCreateHandler $data")
        handleGuild(data)
    }

    private fun handleGuild(data: JsonObject) {
        val guildId = data.getOrNull("guild_id")?.asLong ?: return
        val guild = GuildImpl.get(guildId) ?: return
        val channelId = data["channel_id"].asLong
        val channel = guild.getTextChannel(channelId) ?: return
        val authorObject = data["author"].asJsonObject
        val author = UserImpl(authorObject)
        val memberObject = data["member"].asJsonObject
        val member = MemberImpl(memberObject, author)
        val content = data["content"].asString
        val mentionMembers = getMentionMembers(data)
        val mentionRoles = getMentionRoles(data)
        val message = Message(guild, channel, member, content, mentionMembers, mentionRoles)
        KtDiscord.messageReceiveEvent.invoke(message)
    }

    private fun getMentionMembers(parent: JsonObject): List<Member> {
        val array = parent.getArrayOrNull("mentions")
        return array?.map {
            val data = it.asJsonObject
            val user = UserImpl(data)
            val memberObject = data["member"].asJsonObject
            MemberImpl(memberObject, user)
        } ?: emptyList()
    }

    private fun getMentionRoles(parent: JsonObject): List<Long> {
        return parent.getArrayOrNull("mention_roles")?.map { it.asLong } ?: emptyList()
    }
}